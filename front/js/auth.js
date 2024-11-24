// 리버스 프록시 적용으로 인한 API 엔드포인트 전역 설정
const API_BASE_URL = '/api';

document.getElementById("login-form")?.addEventListener("submit", async (event) => {
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch(`${API_BASE_URL}/users/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
      credentials: "include", // 세션 기반 인증을 위해 반드시 추가
    });

    const responseData = await response.json();

    if (responseData.code === 200) {
      // 로그인 성공 시 Todo 페이지로 이동
      window.location.href = "todo.html";
    } else {
      // 로그인 실패 시 에러 메시지 표시
      document.getElementById("login-error-message").textContent =
        responseData.message || "Login failed";
    }
  } catch (error) {
    console.error("Error logging in:", error);
    document.getElementById("login-error-message").textContent =
      "An error occurred during login.";
  }
});

// 회원가입
document.getElementById("register-form")?.addEventListener("submit", async (event) => {
  event.preventDefault();

  const name = document.getElementById("name").value.trim();
  const email = document.getElementById("register-email").value.trim();
  const password = document.getElementById("register-password").value.trim();

  if (!name || !email || !password) {
    document.getElementById("register-error-message").textContent =
      "All fields are required.";
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/users/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name, email, password }),
    });

    const responseData = await response.json();

    if (responseData.code === 200) {
      // 회원가입 성공
      alert("회원가입 성공!");
      window.location.href = "/";
    } else {
      // 회원가입 실패 메시지 표시
      document.getElementById("register-error-message").textContent =
        responseData.message || "Registration failed.";
    }
  } catch (error) {
    console.error("Error registering user:", error);
    document.getElementById("register-error-message").textContent =
      "An error occurred during registration.";
  }
});

// 로그아웃
document.getElementById("logout-button").addEventListener("click", async () => {
  if(!confirm("로그아웃 하시겠습니까?")) return;

  try {
    const response = await fetch(`${API_BASE_URL}/users/logout`, {
      method: "POST",
      credentials: "include",
    });

    if (response.ok) {
      showToast("로그아웃 되었습니다");
      setTimeout(() => {
        window.location.href = "/"; // 로그아웃 후 로그인 페이지로 이동
      }, 1000);
    } else {
      console.error("Failed to logout:", await response.json());
      alert("Failed to logout. Please try again.");
    }
  } catch (error) {
    console.error("Error during logout:", error);
    alert("An error occurred during logout.");
  }
});

// 회원탈퇴
document.getElementById("user-delete").addEventListener("click", async () => {
  if(!confirm("탈퇴 하시겠습니까? 사용자의 정보가 모두 삭제됩니다.")) return;
  console.log("회원탈퇴 요청");
  try {
    const response = await fetch(`${API_BASE_URL}/users`, {
      method: "DELETE",
      credentials: "include",
    });
    const responseData = await response.json();
    console.log("응답", responseData);
    if (response.ok) {
      showToast("회원과 해당 회원의 데이터가 삭제되었습니다.");
      setTimeout(() => {
        window.location.href = "/";
      }, 1000);
    } else {
      console.error("Failed to delete:", await response.json());
      alert("Failed to delete. Please try again.");
    }
  } catch (error) {
    console.error("Error during delete user:", error);
    alert("An error occurred during delete.");
  }
});
