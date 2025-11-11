// ===================================
// API Communication Utility
// ===================================

const API_BASE_URL = 'http://localhost:8080/apis';

class API {
    /**
     * Make an authenticated API request
     */
    static async request(endpoint, options = {}) {
        const token = Auth.getToken();

        const config = {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            }
        };

        // Add auth token if available
        if (token && !options.skipAuth) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        // Remove Content-Type for FormData
        if (options.body instanceof FormData) {
            delete config.headers['Content-Type'];
        }

        try {
            showLoading();
            const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
            const data = await response.json().catch(() => null);

            hideLoading();

            if (!response.ok) {
                if (response.status === 401) {
                    Auth.logout();
                    window.location.href = 'login.html';
                    throw new Error('Sessão expirada. Faça login novamente.');
                }
                throw new Error(data?.message || 'Erro na requisição');
            }

            return data;
        } catch (error) {
            hideLoading();
            throw error;
        }
    }

    /**
     * GET request
     */
    static get(endpoint, options = {}) {
        return this.request(endpoint, { ...options, method: 'GET' });
    }

    /**
     * POST request
     */
    static post(endpoint, body, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'POST',
            body: body instanceof FormData ? body : JSON.stringify(body)
        });
    }

    /**
     * PUT request
     */
    static put(endpoint, body, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'PUT',
            body: body instanceof FormData ? body : JSON.stringify(body)
        });
    }

    /**
     * DELETE request
     */
    static delete(endpoint, options = {}) {
        return this.request(endpoint, { ...options, method: 'DELETE' });
    }

    // ===================================
    // Public Endpoints
    // ===================================

    static login(credentials) {
        return this.post('/public/login', credentials, { skipAuth: true });
    }

    static register(userData) {
        return this.post('/public/add-user', userData, { skipAuth: true });
    }

    static searchAnuncios(keyword = '') {
        const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : '';
        return this.get(`/public/anuncio/get-filter${query}`, { skipAuth: true });
    }

    static getAnuncioPublic(id) {
        return this.get(`/public/anuncio/get-one/${id}`, { skipAuth: true });
    }

    static sendInteresse(anuncioId, interesseData) {
        return this.post(`/public/mensagem/${anuncioId}`, interesseData, { skipAuth: true });
    }

    // ===================================
    // Prestador Endpoints
    // ===================================

    static getCategorias() {
        return this.get('/prestador/get-all-cat');
    }

    static getMyAnuncios() {
        return this.get('/prestador/get-anuncios');
    }

    static getMyAnuncio(id) {
        return this.get(`/prestador/get-anuncio/${id}`);
    }

    static createAnuncio(anuncioData) {
        return this.post('/prestador', anuncioData);
    }

    static updateAnuncio(anuncioData) {
        return this.put('/prestador', anuncioData);
    }

    static deleteAnuncio(id) {
        return this.delete(`/prestador/${id}`);
    }

    static getProfile() {
        return this.get('/prestador/dados');
    }

    static updateProfile(profileData) {
        return this.put('/prestador/dados', profileData);
    }

    static getMensagens() {
        return this.get('/prestador/mensagens');
    }

    static deleteMensagem(id) {
        return this.delete(`/prestador/mensagem/${id}`);
    }

    static uploadFoto(anuncioId, file) {
        const formData = new FormData();
        formData.append('file', file);
        return this.post(`/prestador/anuncio/${anuncioId}/foto`, formData);
    }

    static deleteFoto(fotoId) {
        return this.delete(`/prestador/foto/${fotoId}`);
    }

    // ===================================
    // Admin Endpoints
    // ===================================

    static getAllCategorias() {
        return this.get('/admin/get-all-cat');
    }

    static getCategoria(id) {
        return this.get(`/admin/get-cat/${id}`);
    }

    static createCategoria(categoriaData) {
        return this.post('/admin', categoriaData);
    }

    static updateCategoria(categoriaData) {
        return this.put('/admin', categoriaData);
    }

    static deleteCategoria(id) {
        return this.delete(`/admin/cat/${id}`);
    }

    static getAllAnunciosAdmin() {
        return this.get('/admin/anuncios');
    }

    static deleteAnuncioAdmin(id) {
        return this.delete(`/admin/anuncio/${id}`);
    }
}
