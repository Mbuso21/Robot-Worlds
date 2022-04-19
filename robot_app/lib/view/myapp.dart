import 'package:flutter/material.dart';
import 'package:robot_app/view/home.dart';

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ROBOT WORLDS',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        
      ),
      home: const MyHomePage(title: 'ROBOT WORLDS'),
    );
  }
}

