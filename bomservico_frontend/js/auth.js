// ===================================
// Authentication Utility
// ===================================

class Auth {
    static TOKEN_KEY = 'bomservico_token';
    static USER_KEY = 'bomservico_user';

    /**
     * Save authentication data
     */
    static login(loginResponse) {
        localStorage.setItem(this.TOKEN_KEY, loginResponse.token);
        localStorage.setItem(this.USER_KEY, JSON.stringify({
            login: loginResponse.login,
            nome: loginResponse.nome,
            nivel: loginResponse.nivel
        }));
    }

    /**
     * Clear authentication data
     */
    static logout() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        window.location.href = 'index.html';
    }

    /**
     * Get stored token
     */
    static getToken() {
        return localStorage.getItem(this.TOKEN_KEY);
    }

    /**
     * Get stored user data
     */
    static getUser() {
        const userStr = localStorage.getItem(this.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    }

    /**
     * Check if user is authenticated
     */
    static isAuthenticated() {
        return !!this.getToken();
    }

    /**
     * Check if user is admin
     */
    static isAdmin() {
        const user = this.getUser();
        return user && user.nivel === 1;
    }

    /**
     * Check if user is prestador
     */
    static isPrestador() {
        const user = this.getUser();
        return user && user.nivel === 0;
    }

    /**
     * Require authentication - redirect if not authenticated
     */
    static requireAuth() {
        if (!this.isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }
        return true;
    }

    /**
     * Require admin role - redirect if not admin
     */
    static requireAdmin() {
        if (!this.isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }
        if (!this.isAdmin()) {
            showToast('Acesso negado. Apenas administradores podem acessar esta página.', 'error');
            window.location.href = 'index.html';
            return false;
        }
        return true;
    }

    /**
     * Require prestador role - redirect if not prestador
     */
    static requirePrestador() {
        if (!this.isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }
        if (!this.isPrestador()) {
            showToast('Acesso negado. Apenas prestadores podem acessar esta página.', 'error');
            window.location.href = 'index.html';
            return false;
        }
        return true;
    }

    /**
     * Update navbar based on authentication status
     */
    static updateNavbar() {
        const navbarMenu = document.querySelector('.navbar-menu');
        if (!navbarMenu) return;

        const user = this.getUser();

        if (this.isAuthenticated()) {
            const dashboardLink = this.isAdmin() ?
                '<a href="admin-dashboard.html" class="navbar-link">Dashboard</a>' :
                '<a href="prestador-dashboard.html" class="navbar-link">Meu Painel</a>';

            const meusAnunciosLink = this.isPrestador() ?
                '<a href="meus-anuncios.html" class="navbar-link">Meus Anúncios</a>' : '';

            navbarMenu.innerHTML = `
                <li><a href="index.html" class="navbar-link">Início</a></li>
                <li>${dashboardLink}</li>
                ${meusAnunciosLink}
                <li><span class="navbar-link">Olá, ${user.nome}</span></li>
                <li><a href="#" onclick="Auth.logout()" class="navbar-link">Sair</a></li>
            `;
        } else {
            navbarMenu.innerHTML = `
                <li><a href="index.html" class="navbar-link">Início</a></li>
                <li><a href="login.html" class="navbar-link">Entrar</a></li>
                <li><a href="cadastro-prestador.html" class="btn btn-primary btn-sm">Seja um Prestador</a></li>
            `;
        }
    }
}

// Update navbar on page load
document.addEventListener('DOMContentLoaded', () => {
    Auth.updateNavbar();
});
