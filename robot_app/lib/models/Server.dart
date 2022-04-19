class Server {
  late int id;
  final String name;
  final String ipAddress;
  final int port;

  Server({int? id, required this.name, required this.ipAddress,
        required this.port});

  factory Server.fromJson(Map<String, dynamic> json) {
    return Server(id: json['id'], name: json['name'], 
                  ipAddress: json['ip'], port: json['port']);
  }
}