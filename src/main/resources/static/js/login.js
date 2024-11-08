function showLogin() {
           document.getElementById("login-form").classList.add("active");
           document.getElementById("register-form").classList.remove("active");
           document.getElementById("login-tab").classList.add("active");
           document.getElementById("register-tab").classList.remove("active");
       }

       function showRegister() {
           document.getElementById("login-form").classList.remove("active");
           document.getElementById("register-form").classList.add("active");
           document.getElementById("login-tab").classList.remove("active");
           document.getElementById("register-tab").classList.add("active");
}