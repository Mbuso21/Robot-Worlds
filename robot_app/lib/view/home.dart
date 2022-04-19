import 'package:flutter/material.dart';
import 'package:robot_app/view/player.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  @override
  Widget build(BuildContext context) {

    final ButtonStyle style =
        ElevatedButton.styleFrom(textStyle: const TextStyle(fontSize: 20));

    return Scaffold(
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          // mainAxisSize: MainAxisSize.max,
          children: <Widget>[
            
            const Text(
              'ROBOT WORLDS',
              style: TextStyle(
                wordSpacing: 10,
                fontSize: 30,
              ),
            ),
            // Size between the Text and button
            const SizedBox(height: 50),
            // ElevatedButton to take the player to the player page
            ElevatedButton(
              style: style,
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) => Player()));
              }, 
              child: const Text('PLAYER'),
            ),
            // Space between the ElevatedButtons
            const SizedBox(height: 50),
            ElevatedButton(
              style: style,
              onPressed: () {}, 
              child: const Text('ADMIN'),
            ),
          ],
        ),
      ),
      
    );
  }
}