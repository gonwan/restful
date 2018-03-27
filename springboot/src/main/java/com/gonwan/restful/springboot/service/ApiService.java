package com.gonwan.restful.springboot.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gonwan.restful.springboot.RestfulException;
import com.gonwan.restful.springboot.RestfulException.Predefined;
import com.gonwan.restful.springboot.RestfulRepository;
import com.gonwan.restful.springboot.model.TApi;
import com.gonwan.restful.springboot.request.GetRowCountRequest;

@Service
public class ApiService {

    @Autowired
    private RestfulRepository repo;

    public TApi getApi(GetRowCountRequest request) throws RestfulException {
        boolean useWhere = (request.getApiVersion() == 0);
        List<TApi> apis;
        if (useWhere) {
            apis = repo.getApis().stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(x.getApiName(), request.getApiName())
                            && Objects.equals(x.getWhereAvailable(), Byte.valueOf((byte)1)))
                    .collect(Collectors.toList());
        } else {
            apis = repo.getApis().stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(x.getApiName(), request.getApiName())
                            && Objects.equals(x.getVersion(), request.getApiVersion()))
                    .collect(Collectors.toList());
        }
        if (apis.size() == 0) {
            throw Predefined.USER_NO_API_VERSION;
        }
        if (apis.size() > 1) {
            throw Predefined.USER_MULTIPLE_API_VERSION;
        }
        TApi api = apis.get(0);
        return api;
    }

}
