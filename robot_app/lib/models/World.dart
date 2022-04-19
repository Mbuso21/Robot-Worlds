import 'dart:convert';

class World {
  late int id;
  final String name;
  final int size;
  late int mine;
  late int repair;
  late int reload;
  late int visibility;
  late int topLeftY;
  late int bottomRightX;
  late int topLeftX;
  late int bottomRightY;

  World({
    int? id,

      required this.name,
      required this.size,
      });

  factory World.fromJson(Map<String, dynamic> json) {
    return World(
        id: json['id'],
        name: json['name'],
        size: json['size']);
  }
}
