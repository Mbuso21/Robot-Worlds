import 'dart:ffi';

class Command {
  final String robotName;
  final String commandName;
  final String arguments;

  Command(
    {required this.robotName, 
    required this.commandName,
    required this.arguments});

  factory Command.fromJson(Map<String, dynamic> json){
    return Command(
      robotName: json['robot'], 
      commandName: json['command'], 
      arguments: json['arguments']);
  }
}