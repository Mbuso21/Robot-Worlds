import 'package:http/http.dart' as http;
import 'package:robot_app/models/Command.dart';
import 'package:robot_app/models/Robot.dart';

import 'dart:convert';

/**
 * RobotService class
 */
class RobotService {
  var _response;
  final String apiUrl = 'http://localhost:8080/';
  final headers = {
    'Access-Control-Allow-Methods': 'GET, POST, DELETE',
    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
    "Content-Type": "application/json"
  };

  Future<List<Robot>> getRobots() async {
    final _response = await http.get(Uri.parse("${apiUrl}admin/robots"));

    if (_response.statusCode == 200) {
      return List<Robot>.from(
          json.decode(_response.body).map((robot) => Robot.fromJson(robot)));
    } else {
      throw Exception('Unable to fetch robots from the API');
    }
  }

  Future<List<Robot>> getRobotsByWorldId(int worldId) async {
    List<Robot> robotsList = List.empty();
    try {
      getRobots().then((robots) => {
            robotsList =
                robots.where((robot) => robot.world_id == worldId).toList()
          });

      return robotsList;
    } catch (Err) {
      throw Err;
    }
  }

  Future<Robot> getRobot(String name) async {
    _response = await http.get(Uri.parse("${apiUrl}admin/robot/$name"));

    if (_response.statusCode == 200) {
      return Robot.fromJson(json.decode(_response.body));
    } else {
      // msg = getMessage(response.statusCode);
      throw Exception('Unable to fetch robot from the API') /**msg */;
    }
  }

  Future<Robot> addRobot(Robot robot) async {
    _response = await http.post(Uri.parse("${apiUrl}admin/robots"),
        headers: headers, body: json.encode(robot));

    if (_response.statusCode == 201) {
      String name = json.decode(_response.body)['name'];
      return getRobot(name);
    } else {
      throw Exception('Unable to add robot from the API');
    }
  }

  Future<Map> command(Command command) async {
    _response = await http.post(Uri.parse("${apiUrl}player/robot"),
        headers: headers, body: json.encode(command));

    if (_response.statusCode == 200) {
      return json.decode(_response.body) as Map;
    } else {
      //getMessage
      throw Exception("");
    }
  }
}
