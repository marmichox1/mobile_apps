import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:todo_list/app.dart';
import 'package:todo_list/utils/preferences_service.dart';

void main() {
  testWidgets('Add and remove todo item test', (WidgetTester tester) async {
    
    SharedPreferences.setMockInitialValues({});
    final prefs = await SharedPreferences.getInstance();
    final preferencesService = PreferencesService(prefs);

    
    await tester.pumpWidget(ToDoApp(preferencesService: preferencesService));

    
    expect(find.text('My Tasks'), findsOneWidget);
    expect(find.byType(ListTile), findsNothing);

   
    await tester.enterText(find.byType(TextField), 'New Todo Item');
    await tester.testTextInput.receiveAction(TextInputAction.done);
    await tester.pump();

   
    expect(find.text('New Todo Item'), findsOneWidget);

    
    await tester.tap(find.byIcon(Icons.delete));
    await tester.pump();

    
    expect(find.text('New Todo Item'), findsNothing);
  });

  testWidgets('Toggle theme test', (WidgetTester tester) async {
   
    SharedPreferences.setMockInitialValues({});
    final prefs = await SharedPreferences.getInstance();
    final preferencesService = PreferencesService(prefs);

   
    await tester.pumpWidget(ToDoApp(preferencesService: preferencesService));

   
    expect(Theme.of(tester.element(find.byType(MaterialApp))).brightness, equals(Brightness.light));

   
    await tester.tap(find.byIcon(Icons.dark_mode));
    await tester.pumpAndSettle();

   
    expect(Theme.of(tester.element(find.byType(MaterialApp))).brightness, equals(Brightness.dark));
  });
}