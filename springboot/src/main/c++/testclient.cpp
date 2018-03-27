/* g++ testclient.cpp -o testclient -lcppnetlib-client-connections -lcppnetlib-uri -lboost_system -lboost_thread -lpthread */
#include <iostream>
#include <istream>
#include <ostream>
#include <string>
#include <boost/network/protocol.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
using namespace std;

const static string SERVER_ADDRESS = "192.168.11.196:5050";
const static string LOGIN_PATH = "/api/login";
const static string RUNAPI_PATH = "/api/runapi";
const static string LOGIN_REQUEST = "{ \"username\": \"test2\", \"password\": \"123456\" }";
const static string RUNAPI_REQUEST = string()
            + "{ \"apiName\": \"HOLIDAY_INFO\", "
            + "\"apiVersion\": 0, "
            + "\"username\": \"test2\", "
            + "\"password\": \"123456\", "
            + "\"conditions\": null, "
            + "\"startDate\": \"\", "
            + "\"endDate\": \"\", "
            + "\"startPage\": 1, "
            + "\"pageSize\": 3, "
            + "\"columns\": null }";

int main() {

    using std::string;
    using namespace boost::network;
    using namespace boost::network::http;
    using namespace boost::property_tree;

    try {
        client client;
        /* login */
        cout << "--- testing login ---" << endl;
        client::request login_request("http://" + SERVER_ADDRESS + LOGIN_PATH);
        login_request << header("Connection", "close");
        client::response login_response = client.post(login_request, LOGIN_REQUEST, string("application/json"));
        cout << "status=" << status(login_response) << endl;
        ptree login_body;
        stringstream login_ss;
        login_ss << body(login_response);
        read_json(login_ss, login_body);
        int code = login_body.get<int>("code");
        string message = login_body.get<string>("message");
        cout << "code=" << code << " message=" << message << endl;
        /* runapi */
        cout << "--- testing runapi ---" << endl;
        client::request runapi_request("http://" + SERVER_ADDRESS + RUNAPI_PATH);
        runapi_request << header("Connection", "close");
        client::response runapi_response = client.post(runapi_request, RUNAPI_REQUEST, string("application/json"));
        cout << "status=" << status(runapi_response) << endl;
        ptree runapi_body;
        stringstream runapi_ss;
        runapi_ss << body(runapi_response);
        read_json(runapi_ss, runapi_body);
        code = runapi_body.get<int>("code");
        message = runapi_body.get<string>("message");
        ptree result_table = runapi_body.get_child("resultTable");
        stringstream oss;
        write_json(oss, result_table);
        cout << "code=" << code << " message=" << message << " resultTable=" << oss.str() << endl;
    } catch (exception &e) {
        cerr << "exception: " << e.what() << endl;
    }
    return 0;
}
