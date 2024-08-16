class TodoItem {
  final String title;

  TodoItem({required this.title});

  Map<String, dynamic> toJson() => {'title': title};

  factory TodoItem.fromJson(Map<String, dynamic> json) {
    return TodoItem(title: json['title']);
  }
}