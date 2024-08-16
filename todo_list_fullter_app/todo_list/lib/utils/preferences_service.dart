import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import '../models/todo_item.dart';

class PreferencesService {
  final SharedPreferences _prefs;

  PreferencesService(this._prefs);

  bool get isDarkMode => _prefs.getBool('isDarkMode') ?? false;
  set isDarkMode(bool value) => _prefs.setBool('isDarkMode', value);

  List<TodoItem> getTodos() {
    final String? todosJson = _prefs.getString('todos');
    if (todosJson != null) {
      final List<dynamic> todosList = json.decode(todosJson);
      return todosList.map((item) => TodoItem.fromJson(item)).toList();
    }
    return [];
  }

  void saveTodos(List<TodoItem> todos) {
    final String todosJson = json.encode(todos.map((todo) => todo.toJson()).toList());
    _prefs.setString('todos', todosJson);
  }
}