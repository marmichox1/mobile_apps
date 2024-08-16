import 'package:flutter/material.dart';
import 'screens/todo_list_screen.dart';
import 'utils/preferences_service.dart';

class ToDoApp extends StatefulWidget {
  final PreferencesService preferencesService;

  ToDoApp({required this.preferencesService});

  @override
  _ToDoAppState createState() => _ToDoAppState();
}

class _ToDoAppState extends State<ToDoApp> {
  late bool _isDarkMode;

  @override
  void initState() {
    super.initState();
    _isDarkMode = widget.preferencesService.isDarkMode;
  }

  void _toggleTheme() {
    setState(() {
      _isDarkMode = !_isDarkMode;
      widget.preferencesService.isDarkMode = _isDarkMode;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Liste de Tâches',
      theme: _isDarkMode 
        ? ThemeData.dark().copyWith(
            colorScheme: ColorScheme.dark(
              primary: Colors.deepPurple,
              secondary: Colors.deepPurpleAccent,
            ),
            floatingActionButtonTheme: FloatingActionButtonThemeData(
              backgroundColor: Colors.deepPurpleAccent,
            ),
          )
        : ThemeData.light().copyWith(
            colorScheme: ColorScheme.light(
              primary: Colors.deepPurple,
              secondary: Colors.deepPurpleAccent,
            ),
            floatingActionButtonTheme: FloatingActionButtonThemeData(
              backgroundColor: Colors.deepPurpleAccent,
            ),
          ),
      home: ToDoListScreen(
        preferencesService: widget.preferencesService,
        toggleTheme: _toggleTheme,
        isDarkMode: _isDarkMode,
      ),
    );
  }
}