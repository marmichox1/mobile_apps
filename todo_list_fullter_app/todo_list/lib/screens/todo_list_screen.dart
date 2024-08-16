import 'package:flutter/material.dart';
import '../models/todo_item.dart';
import '../widgets/todo_list_item.dart';
import '../widgets/add_todo_input.dart';
import '../utils/preferences_service.dart';

class ToDoListScreen extends StatefulWidget {
  final PreferencesService preferencesService;
  final Function toggleTheme;
  final bool isDarkMode;

  ToDoListScreen({
    required this.preferencesService,
    required this.toggleTheme,
    required this.isDarkMode,
  });

  @override
  _ToDoListScreenState createState() => _ToDoListScreenState();
}

class _ToDoListScreenState extends State<ToDoListScreen> {
  List<TodoItem> _todos = [];

  @override
  void initState() {
    super.initState();
    _loadTodos();
  }

  void _loadTodos() {
    setState(() {
      _todos = widget.preferencesService.getTodos();
    });
  }

  void _addTodo(String title) {
    if (title.isNotEmpty) {
      setState(() {
        _todos.add(TodoItem(title: title));
        widget.preferencesService.saveTodos(_todos);
      });
    }
  }

  void _removeTodo(int index) {
    setState(() {
      _todos.removeAt(index);
      widget.preferencesService.saveTodos(_todos);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Mes Tâches'),
        elevation: 0,
        actions: [
          IconButton(
            icon: Icon(widget.isDarkMode ? Icons.light_mode : Icons.dark_mode),
            onPressed: () => widget.toggleTheme(),
          ),
        ],
      ),
      body: _todos.isEmpty
          ? Center(child: Text('Aucune tâche. Ajoutez-en une !'))
          : ListView.builder(
              itemCount: _todos.length,
              itemBuilder: (context, index) {
                return TodoListItem(
                  todo: _todos[index],
                  onDelete: () => _removeTodo(index),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddTodoDialog(context),
        child: Icon(Icons.add),
        tooltip: 'Ajouter une tâche',
      ),
    );
  }

  void _showAddTodoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Ajouter une nouvelle tâche'),
        content: AddTodoInput(onSubmit: (title) {
          _addTodo(title);
          Navigator.of(context).pop();
        }),
        actions: [
          TextButton(
            child: Text('Annuler'),
            onPressed: () => Navigator.of(context).pop(),
          ),
        ],
      ),
    );
  }
}