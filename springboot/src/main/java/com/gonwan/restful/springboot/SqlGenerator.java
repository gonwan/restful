package com.gonwan.restful.springboot;

import java.io.StringReader;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonwan.restful.springboot.RestfulException.Predefined;
import com.gonwan.restful.springboot.model.TApi;
import com.gonwan.restful.springboot.request.GetRowCountRequest;
import com.gonwan.restful.springboot.request.RunApiRequest;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;


public class SqlGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private static final String SQL_COLUMNS = "[columns]";

    private static final String SQL_DUMMY_TABLE = "_dummy_table_";

    private static final String SQL_DUMMY_QUERY = "select * from " + SQL_DUMMY_TABLE + " ";

    private static final List<String> SQL_KEYWORDS = Arrays.asList(new String[]{ "insert", "delete", "update", "select", "create", "drop", "alter", ";" });

    public static void checkSqlInjection(String sql) throws RestfulException {
        List<String> tokens = Arrays.asList(StringUtils.split(sql));
        Optional<String> f = tokens.stream()
                .map(x -> x.toLowerCase())
                .filter(x -> SQL_KEYWORDS.contains(x))
                .findAny();
        if (f.isPresent()) {
            throw Predefined.REQ_INVALID_QUERY_CONDITION;
        }
    }

    public static SQLTemplates getQueryDSLTemplates(Byte dbDialect) {
        SQLTemplates.Builder builder = null;
        switch (dbDialect) {
            case 1:
                builder = OracleTemplates.builder();
                break;
            case 2:
                builder = SQLServerTemplates.builder();
                break;
            case 3:
            default:
                builder = MySQLTemplates.builder();
                break;
        }
        return builder
                //.printSchema()
                //.quote()
                .build();
    }

    public static String get(GetRowCountRequest request, TApi api, Byte dbDialect) throws RestfulException {
        SQLTemplates sqlTemplates = getQueryDSLTemplates(dbDialect);
        Configuration configuration = new Configuration(sqlTemplates);
        SQLQuery<Void> sqlQuery = new SQLQuery<>(configuration);
        sqlQuery.setUseLiterals(true);  /* to use parameter bindings */
        /* parse */
        Select select = null;
        try {
            Statement statement = CCJSqlParserUtil.parse(api.getLogicSql());
            if (statement instanceof Select) {
                select = (Select)statement;
            } else {
                throw RestfulException.Predefined.SYS_DATASOURCE_INVALID_SQL;
            }
        } catch (JSQLParserException e) {
            logger.warn("SQL parse error", e);
            throw RestfulException.Predefined.SYS_DATASOURCE_INVALID_SQL;
        }
        /* replace */
        StringBuilder stringBuilder = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser() {
            @Override
            public void visit(Column tableColumn) {
                if (tableColumn.getColumnName().equals(SQL_COLUMNS)) {
                    if (!(request instanceof RunApiRequest)) {
                        stringBuilder.append("count(*)");
                    } else {
                        String columns = "*";
                        if (request.getColumns().length != 0) {
                            List<String> cols = Arrays.asList(request.getColumns()).stream()
                                    .map(x -> sqlTemplates.quoteIdentifier(x))
                                    .collect(Collectors.toList());
                            columns = StringUtils.join(cols, ", ");
                        }
                        stringBuilder.append(columns);
                    }
                }
            }
        };
        SelectDeParser selectDeParser = new SelectDeParser(expressionDeParser, stringBuilder);
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(stringBuilder);
        select.getSelectBody().accept(selectDeParser);
        logger.debug("Parsed SQL: {}", stringBuilder.toString());

        /* select ... from ... */
        sqlQuery.select(ExpressionUtils.predicateTemplate(stringBuilder.toString()));
        sqlQuery.from(Expressions.stringPath(null, SQL_DUMMY_TABLE));
        //Path<Object> rootPath = new RelationalPathBase<>(Object.class, "var", "", "");
        /* dates */
        String[] arrDateColumn = StringUtils.split(StringUtils.trimToEmpty(api.getDateColumn()), ":");
        if (arrDateColumn.length > 0) {
            if (request.getStartDate() != null || request.getEndDate() != null) {
                String colName = arrDateColumn[0];
                String colType = (arrDateColumn.length > 1) ? arrDateColumn[1].toLowerCase() : "";
                String colFormat = (arrDateColumn.length > 2) ? arrDateColumn[2].toLowerCase() : ""; /* regards as java date format */
                switch (colType) {
                    case "date": {
                        DatePath<Date> datePath = Expressions.datePath(Date.class, null, colName);
                        if (request.getStartDate() != null) {
                            Date dt = Date.valueOf(request.getStartDate());
                            sqlQuery.where(
                                    datePath.goe(dt)
                            );
                        }
                        if (request.getEndDate() != null) {
                            Date dt = Date.valueOf(request.getEndDate());
                            sqlQuery.where(
                                    datePath.loe(dt)
                            );
                        }
                        break;
                    }
                    case "number": {
                        NumberPath<Integer> datePath = Expressions.numberPath(Integer.class, null, colName);
                        if (request.getStartDate() != null) {
                            Integer dt = request.getStartDate().getYear() * 10000
                                    + request.getStartDate().getMonthValue() * 100
                                    + request.getStartDate().getDayOfMonth();
                            sqlQuery.where(
                                    datePath.goe(dt)
                            );
                        }
                        if (request.getEndDate() != null) {
                            Integer dt = request.getEndDate().getYear() * 10000
                                    + request.getEndDate().getMonthValue() * 100
                                    + request.getEndDate().getDayOfMonth();
                            sqlQuery.where(
                                    datePath.loe(dt)
                            );
                        }
                        break;
                    }
                    case "string": default: {
                        StringPath datePath = Expressions.stringPath(null, colName);
                        if (colFormat.isEmpty()) {
                            logger.warn("Empty column format: {}", api.getApiName());
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(colFormat);
                            if (request.getStartDate() != null) {
                                String dt = formatter.format(request.getStartDate());
                                sqlQuery.where(
                                        datePath.goe(dt)
                                );
                            }
                            if (request.getEndDate() != null) {
                                String dt = formatter.format(request.getEndDate());
                                sqlQuery.where(
                                        datePath.loe(dt)
                                );
                            }
                        }
                        break;
                    }
                }
            }
        }
        /* mandatory conditions */
        if (!StringUtils.isEmpty(api.getMandatoryCondition())) {
            try {
                Expression conds = CCJSqlParserUtil.parseCondExpression(api.getMandatoryCondition());
                sqlQuery.where(
                        ExpressionUtils.predicateTemplate(conds.toString())
                );
            } catch (JSQLParserException e) {
                logger.warn("", e);
                throw RestfulException.Predefined.SYS_DATASOURCE_INVALID_SQL;
            }
        }
        /* conditions */
        if (api.getWhereAvailable() != null && api.getWhereAvailable() != 0) {
            if (!request.getConditions().isEmpty()) {
                try {
                    CCJSqlParser sqlParser = null;
                    String c = request.getConditions().toLowerCase().trim();
                    if (c.startsWith("group by") || c.startsWith("order by")) {
                        sqlParser = new CCJSqlParser(new StringReader(SQL_DUMMY_QUERY + request.getConditions()));
                    } else {
                        sqlParser = new CCJSqlParser(new StringReader(SQL_DUMMY_QUERY + "where " + request.getConditions()));
                    }
                    PlainSelect plainSelect = sqlParser.PlainSelect();
                    Expression where = plainSelect.getWhere();
                    if (where != null) {
                        sqlQuery.where(
                                ExpressionUtils.predicateTemplate(where.toString())
                        );
                    }
                    List<Expression> groupBy = plainSelect.getGroupByColumnReferences();
                    if (groupBy != null) {
                        for (Expression e : groupBy) {
                            sqlQuery.groupBy(Expressions.stringPath(e.toString()));
                        }
                    }
                    Expression having = plainSelect.getHaving();
                    if (having != null) {
                        sqlQuery.having(ExpressionUtils.predicateTemplate(having.toString()));
                    }
                    List<OrderByElement> orderBy = plainSelect.getOrderByElements();
                    if (orderBy != null) {
                        for (OrderByElement e : orderBy) {
                            sqlQuery.orderBy(new OrderSpecifier<>(
                                    e.isAsc() ? Order.ASC : Order.DESC,
                                    Expressions.stringPath(e.getExpression().toString())));
                        }
                    }
                } catch (ParseException e) {
                    logger.warn("", e);
                    throw RestfulException.Predefined.REQ_INVALID_QUERY_CONDITION;
                }
            }
        }
        if (request instanceof RunApiRequest) {
            RunApiRequest req = (RunApiRequest)request;
            int offset = (req.getStartPage() - 1) * req.getPageSize();
            int limit = req.getPageSize();
            sqlQuery.offset(offset).limit(limit);
        }
        /* cleanup 'select' and 'from' */
        String sql = sqlQuery.getSQL().getSQL();
        sql = sql.substring(sqlTemplates.getSelect().length());
        sql = StringUtils.replaceOnce(sql, sqlTemplates.getFrom() + SQL_DUMMY_TABLE, "");
        logger.debug("Final SQL: {}", sql);
        return sql;
    }

}
