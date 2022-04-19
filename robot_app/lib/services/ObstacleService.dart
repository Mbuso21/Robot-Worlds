import 'package:http/http.dart' as http;

import 'dart:convert';

import 'package:robot_app/models/Obstacle.dart';

/**
 * ObstacleService class
 */
class ObstacleService {
  var _response;
  final String apiUrl = 'http://localhost:8080/';
  final headers = {
    'Access-Control-Allow-Methods': 'GET, POST, DELETE',
    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
    "Content-Type": "application/json"
  };

  Future<List<Obstacle>> getObstacles() async {
    final _response = await http.get(Uri.parse("${apiUrl}admin/obstacles"));

    if (_response.statusCode == 200) {
      return List<Obstacle>.from(
          json.decode(_response.body).map((obs) => Obstacle.fromJson(obs)));
    } else {
      throw Exception('Unable to fetch obstacles from the API');
    }
  }

  Future<String> addObstacles(List<Obstacle> obstacles) async {
    _response = await http.post(Uri.parse("${apiUrl}admin/obstacles"),
        headers: headers, body: obstacles.map((obs) => obs.toJson()));

    if (_response.statusCode == 201) {
      return "Added obstacles successfully.";
    } else {
      return "Failed to add obstacles.";
    }
  }

  Future<String> deleteObstacles(List<Obstacle> obstacles) async {
    _response = await http.delete(Uri.parse("${apiUrl}admin/obstacles"),
        headers: headers, body: obstacles.map((obs) => obs.toJson()));

    if (_response.statusCode == 200) {
      return "Deleted obstacles successfully."; //_response.body['details/message/something']
    } else {
      return "Failed to delete the provided obstacles.";
    }
  }

  Future<List<Obstacle>> getObstaclesByWorldId(int worldId) async {
    List<Obstacle> obstaclesList = List.empty();
    try {
      getObstacles().then((obs) => {
            obstaclesList =
                obs.where((obstacle) => obstacle.world_id == worldId).toList()
          });

      return obstaclesList;
    } catch (Err) {
      throw Err;
    }
  }
}
