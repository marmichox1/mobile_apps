import 'package:flutter/material.dart';
import '../models/todo_item.dart';

class TodoListItem extends StatelessWidget {
  final TodoItem todo;
  final VoidCallback onDelete;

  TodoListItem({required this.todo, required this.onDelete});

  @override
  Widget build(BuildContext context) {
    return Dismissible(
      key: Key(todo.title),
      background: Container(
        color: Colors.red,
        alignment: Alignment.centerRight,
        padding: EdgeInsets.only(right: 20),
        child: Icon(Icons.delete, color: Colors.white),
      ),
      direction: DismissDirection.endToStart,
      onDismissed: (direction) => onDelete(),
      child: ListTile(
        title: Text(
          todo.title,
          style: TextStyle(fontSize: 18),
        ),
        trailing: IconButton(
          icon: Icon(Icons.delete, color: Colors.red),
          onPressed: onDelete,
        ),
      ),
    );
  }
}