import 'package:flutter/material.dart';

class Player extends StatelessWidget {
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
              onPressed: () {}, 
              child: const Text('LAUNCH'),
            ),
            // Space between the ElevatedButtons
          ],
        ),
      ),
      
    );

    
    throw UnimplementedError();
  }
}