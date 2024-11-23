// 단색 색상 클래스 배열
const colors = ["color-red", "color-blue", "color-green", "color-yellow", "color-purple", "color-orange"];
let colorIndex = 0;

// Show toast message
function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.classList.add("show");

    // 1.5초 후 토스트 메시지 사라짐
    setTimeout(() => {
        toast.classList.remove("show");
    }, 1500);
}

// Fetch todos on page load
window.addEventListener("load", async () => {
    try {
        const response = await fetch("http://localhost:8080/todos", {
            method: "GET",
            credentials: "include",
        });

        const responseData = await response.json();

        if (responseData.code === 200) {
            const todos = responseData.data;
            const todoList = document.getElementById("todo-list");
            todoList.innerHTML = ""; // 기존 리스트 초기화

            todos.forEach((todo) => {
                addTodoToDOM(todo);
            });
        } else {
            console.error("Failed to fetch todos:", responseData.message);
            showToast("조회 실패!");
        }
    } catch (error) {
        console.error("Error fetching todos:", error);
        showToast("Error fetching todos.");
    }
});

// Add new todo
document.getElementById("add-todo-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const newTodoText = document.getElementById("new-todo").value.trim(); // 입력 값 확인
    if (!newTodoText) {
        showToast("Please enter a task!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/todos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ content: newTodoText }),
            credentials: "include",
        });

        if (response.ok) {
            const responseData = await response.json();
            const todo = responseData.data; // 서버에서 반환된 todo 객체 사용
            addTodoToDOM(todo); // 새로 추가된 todo를 DOM에 바로 반영
            document.getElementById("new-todo").value = ""; // 입력 필드 초기화
            showToast("할일 추가 성공!");
        } else {
            const errorData = await response.json();
            console.error("Failed to add todo:", errorData.message);
            showToast("문제발생!" + errorData.message);
        }
    } catch (error) {
        console.error("Error adding todo:", error);
        showToast("할일을 추가하는데 문제가 발생했습니다.");
    }
});

// Add todo to DOM
function addTodoToDOM(todo) {
    const todoList = document.getElementById("todo-list");
    const todoItem = document.createElement("li");
    todoItem.setAttribute("data-id", todo.id);
    todoItem.classList.add("todo-item");
  
    // 색상 할당
    const colorDiv = document.createElement("div");
    colorDiv.classList.add("todo-item-color", colors[colorIndex]);
    colorIndex = (colorIndex + 1) % colors.length;
  
    const todoText = document.createElement("span");
    todoText.textContent = todo.content;
    todoText.classList.add("todo-content");

    // 초기 상태로 취소선 설정
    if (todo.completed) {
        todoText.style.textDecoration = "line-through";
        todoText.style.color = "#888";
    }
  
    const todoCheckbox = document.createElement("input");
    todoCheckbox.type = "checkbox";
    todoCheckbox.checked = todo.completed;

    // 체크박스 이벤트에서 취소선 처리
    todoCheckbox.onchange = () => {
        toggleTodoCheck(todo.id, todoCheckbox.checked);
        if (todoCheckbox.checked) {
            todoText.style.textDecoration = "line-through";
            todoText.style.color = "#888";
        } else {
            todoText.style.textDecoration = "none";
            todoText.style.color = "#fff";
        }
    };
  
    const deleteButton = document.createElement("button");
    deleteButton.classList.add("delete");
    deleteButton.innerHTML = `<i class="fas fa-trash"></i>`; // Font Awesome 아이콘 추가
    deleteButton.onclick = () => {
        if (confirm("할 일을 삭제하시겠습니까?")) {
            deleteTodo(todo.id, todoItem);
        }
    };
  
    todoItem.appendChild(colorDiv);
    todoItem.appendChild(todoCheckbox);
    todoItem.appendChild(todoText);
    todoItem.appendChild(deleteButton);
    todoList.appendChild(todoItem);
}


// Toggle todo check
async function toggleTodoCheck(id, isChecked) {
    try {
        const response = await fetch(`http://localhost:8080/todos/${id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ check: isChecked }),
            credentials: "include",
        });

        if (response.ok) {
            showToast("할일 완료!");
        } else {
            console.error("Failed to toggle todo check:", await response.json());
        }
    } catch (error) {
        console.error("Error toggling todo check:", error);
    }
}

// Delete todo
async function deleteTodo(id, todoItem) {
    try {
        const response = await fetch(`http://localhost:8080/todos/${id}`, {
            method: "DELETE",
            credentials: "include",
        });

        if (response.ok) {
            showToast("할 일 삭제!");
            todoItem.remove(); // 삭제된 todo를 DOM에서 제거
        } else {
            console.error("Failed to delete todo:", await response.json());
        }
    } catch (error) {
        console.error("Error deleting todo:", error);
    }
}
