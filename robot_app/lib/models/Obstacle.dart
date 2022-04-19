class Obstacle {
  late int id;
  final int size;
  final String position;
  final int world_id;

  Obstacle(
      {int? id,
      required this.size,
      required this.position,
      required this.world_id});

  factory Obstacle.fromJson(Map<String, dynamic> json) {
    return Obstacle(
        id: json['id'],
        size: json['size'],
        position: json['position'],
        world_id: json['world_id']);
  }

  Map<String, dynamic> toJson() =>
  {
    'id': id,
    'size': size,
    'position': position,
    'world_id': world_id
  }; 
}
