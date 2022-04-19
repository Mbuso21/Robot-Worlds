class Robot {
  late int id;
  final String name;
  final String position;
  final String direction;
  final int world_id;

  Robot(
      {int? id,
      required this.name,
      required this.position,
      required this.direction,
      required this.world_id});

  factory Robot.fromJson(Map<String, dynamic> json) {
    return Robot(
        id: json['id'],
        name: json['name'],
        position: json['position'],
        direction: json['direction'],
        world_id: json['world_id']);
  }
}
