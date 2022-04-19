import 'package:http/http.dart' as http;

import 'dart:convert';

import 'package:robot_app/models/World.dart';
import 'package:robot_app/services/ObstacleService.dart';
import 'package:robot_app/services/RobotService.dart';

/**
 * WorldService class
 */
class WorldService {
  var _response;
  final String apiUrl = 'http://localhost:8080/';
  final headers = {
    'Access-Control-Allow-Methods': 'GET, POST, DELETE',
    'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token',
    "Content-Type": "application/json"
  };
  final RobotService robotService = new RobotService();
  final ObstacleService obstacleService = new ObstacleService();

  Future<World> getWorld(String name) async {
    _response = await http.get(Uri.parse("${apiUrl}admin/load/$name"));

    if (_response.statusCode == 200) {
      return World.fromJson(json.decode(_response.body));
    } else {
      // msg = getMessage(response.statusCode);
      throw Exception('Unable to fetch world from the API') /**msg */;
    }
  }

  Future<World> addWorld(World world) async {
    _response = await http.post(Uri.parse("${apiUrl}admin/save/${world.name}"),
        headers: headers, body: json.encode(world));

    if (_response.statusCode == 201) {
      String name = json.decode(_response.body)['name'];
      return getWorld(name);
    } else {
      throw Exception('Unable to add world from the API');
    }
  }

  Future<Map> getWorldState(String name) async {
    Map<String, dynamic> worldData = {
      "robots": null,
      "obstacles": null,
      "mine": null,
      "repair": null,
      "reload": null,
      "visibility": null,
      "topLeftY": null,
      "bottomRightX": null,
      "topLeftX": null,
      "bottomRightY": null,
    };

    try {
      getWorld(name).then((world) => {
        worldData['robots'] =
            robotService.getRobotsByWorldId(world.id).asStream().toList(),
        worldData['obstacles'] = obstacleService
            .getObstaclesByWorldId(world.id)
            .asStream()
            .toList(),
        // worldData['mine'] = world.mine,
        // worldData['repair'] = world.repair,
        // worldData['reload'] = world.reload,
        // worldData['visibility'] = world.visibility,
        // worldData['topLeftY'] = world.topLeftY,
        // worldData['bottomRightX'] = world.bottomRightX,
        // worldData['topLeftX'] = world.topLeftX,
        // worldData['bottomRightY'] = world.bottomRightY
      });
    } catch (Err) {
      throw Err;
    }

    return worldData;
  }
}
