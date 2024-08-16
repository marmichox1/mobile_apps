import 'package:flutter/material.dart';

class AddTodoInput extends StatefulWidget {
  final Function(String) onSubmit;

  AddTodoInput({required this.onSubmit});

  @override
  _AddTodoInputState createState() => _AddTodoInputState();
}

class _AddTodoInputState extends State<AddTodoInput> {
  final TextEditingController _controller = TextEditingController();

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: _controller,
      decoration: InputDecoration(
        hintText: 'Entrez une nouvelle tâche',
        border: OutlineInputBorder(),
        suffixIcon: IconButton(
          icon: Icon(Icons.clear),
          onPressed: () => _controller.clear(),
        ),
      ),
      onSubmitted: (value) {
        widget.onSubmit(value);
        _controller.clear();
      },
    );
  }
}