import 'package:http/http.dart' as http;
import 'package:robot_app/models/Server.dart';

import 'dart:convert';

/**
 * ServerService class
 */
class ServerService {
  var _response;
  final String apiUrl = 'http://localhost:8080/';
  final headers = {
    'Access-Control-Allow-Methods': 'GET, POST, DELETE',
    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
    "Content-Type": "application/json"
  };

  Future<List<Server>> getServers() async {
    final _response = await http.get(Uri.parse("${apiUrl}admin/servers"));

    if (_response.statusCode == 200) {
      return List<Server>.from(
          json.decode(_response.body).map((server) => Server.fromJson(server)));
    } else {
      throw Exception('Unable to fetch servers from the API');
    }
  }

  Future<Server> getServer(int id) async {
    _response = await http.get(Uri.parse("${apiUrl}admin/server/$id"));

    if (_response.statusCode == 200) {
      return Server.fromJson(json.decode(_response.body));
    } else {
      // msg = getMessage(response.statusCode);
      print(_response);
      throw Exception('Unable to fetch server from the API') /**msg */;
    }
  }

  Future<Server> addServer(Server server) async {
    _response = await http.post(Uri.parse("${apiUrl}admin/servers"),
        headers: headers, body: json.encode(server));

    if (_response.statusCode == 201) {
      int id = int.parse(json.decode(_response.body)['id']);
      return getServer(id);
    } else {
      throw Exception('Unable to add server from the API');
    }
  }

  Future<String> connectToServer(int serverId) async {
    _response = await http.post(Uri.parse("${apiUrl}admin/server/$serverId"),
        headers: headers);

    if (_response.statusCode == 200) {
      return "The connection was successfully established."; //_response.body['details/message/something']
    } else {
      return "The connection could not be established with the selected server.";
    }
  }

  Future<String> deleteServer(int serverId) async {
    _response = await http.delete(Uri.parse("${apiUrl}admin/server/$serverId"),
        headers: headers);

    if (_response.statusCode == 200) {
      return "The server was deleted successfully."; //_response.body['details/message/something']
    } else {
      return "Failed to delete the selected server.";
    }
  }
}
