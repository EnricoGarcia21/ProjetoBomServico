// ServiçosJá - Main JavaScript File

// Global variables
let currentUser = null;
let searchResults = [];
let messages = [];
let ads = [];
let categories = [];

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    checkUserSession();
});

// Initialize application
function initializeApp() {
    console.log('ServiçosJá initialized');
    loadMockData();
    setupFormValidation();
    setupImagePreviews();
}

// Setup event listeners
function setupEventListeners() {
    // Search form
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearch);
    }

    // Login form
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    // Registration form
    const cadastroPrestadorForm = document.getElementById('cadastroPrestadorForm');
    if (cadastroPrestadorForm) {
        cadastroPrestadorForm.addEventListener('submit', handleRegistration);
    }

    // Ad form
    const cadastroAnuncioForm = document.getElementById('cadastroAnuncioForm');
    if (cadastroAnuncioForm) {
        cadastroAnuncioForm.addEventListener('submit', handleAdSubmission);
    }

    // Contact form
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', handleContactSubmission);
    }

    // Filter changes
    const filterElements = document.querySelectorAll('#filterStatus, #filterCategoria, #sortBy');
    filterElements.forEach(element => {
        if (element) {
            element.addEventListener('change', applyFilters);
        }
    });

    // CEP lookup
    const cepInput = document.getElementById('cep');
    if (cepInput) {
        cepInput.addEventListener('blur', lookupCEP);
    }
}

// Load mock data
function loadMockData() {
    // Mock categories
    categories = [
        { id: 1, name: 'Reparos', description: 'Serviços de reparo e manutenção', active: true },
        { id: 2, name: 'Transporte', description: 'Mudanças, entregas e transporte', active: true },
        { id: 3, name: 'Culinária', description: 'Chefs, buffets e serviços culinários', active: true },
        { id: 4, name: 'Limpeza', description: 'Limpeza residencial e comercial', active: true },
        { id: 5, name: 'Jardinagem', description: 'Cuidados com jardins e plantas', active: true },
        { id: 6, name: 'Beleza', description: 'Serviços de beleza e estética', active: true },
        { id: 7, name: 'Educação', description: 'Aulas particulares e cursos', active: true },
        { id: 8, name: 'Tecnologia', description: 'Suporte técnico e desenvolvimento', active: true }
    ];

    // Mock ads
    ads = [
        {
            id: 1,
            title: 'Serviços de Encanamento Profissional',
            description: 'Conserto de vazamentos, instalação de torneiras, desentupimento de pias e vasos sanitários. Atendimento 24h para emergências.',
            category: 'reparos',
            provider: 'João Silva',
            phone: '(11) 99999-9999',
            email: 'joao.silva@email.com',
            location: 'São Paulo - SP',
            price: 'A partir de R$ 80',
            status: 'ativo',
            views: 45,
            date: '2024-10-20'
        },
        {
            id: 2,
            title: 'Instalações Elétricas Residenciais',
            description: 'Instalação de tomadas, troca de fiação, quadros elétricos, iluminação. Eletricista certificado com 10 anos de experiência.',
            category: 'reparos',
            provider: 'João Silva',
            phone: '(11) 99999-9999',
            email: 'joao.silva@email.com',
            location: 'São Paulo - SP',
            price: 'Sob orçamento',
            status: 'ativo',
            views: 62,
            date: '2024-10-18'
        },
        {
            id: 3,
            title: 'Limpeza Residencial Completa',
            description: 'Limpeza profunda de residências, apartamentos e casas. Equipe treinada, produtos de qualidade e preços justos.',
            category: 'limpeza',
            provider: 'Maria Santos',
            phone: '(11) 88888-8888',
            email: 'maria.santos@email.com',
            location: 'São Paulo - SP',
            price: 'R$ 150 por limpeza',
            status: 'ativo',
            views: 33,
            date: '2024-10-15'
        }
    ];

    // Mock messages
    messages = [
        {
            id: 1,
            from: 'Maria Santos',
            email: 'maria.santos@email.com',
            phone: '(11) 88888-8888',
            message: 'Olá! Gostaria de contratar seus serviços de encanamento. Tenho um vazamento na cozinha que precisa ser resolvido urgentemente. Quando você poderia vir dar uma olhada?',
            date: '2024-10-23 14:30',
            read: false
        },
        {
            id: 2,
            from: 'Carlos Oliveira',
            email: 'carlos.oliveira@email.com',
            phone: '(11) 77777-7777',
            message: 'Boa tarde! Vi seu anúncio de serviços elétricos. Preciso instalar algumas tomadas na minha casa. Poderia fazer um orçamento?',
            date: '2024-10-23 10:15',
            read: false
        },
        {
            id: 3,
            from: 'Ana Costa',
            email: 'ana.costa@email.com',
            phone: '(11) 66666-6666',
            message: 'Olá João! Preciso de um orçamento para pintura de um apartamento de 2 quartos. Você trabalha com pintura residencial?',
            date: '2024-10-22 16:45',
            read: false
        }
    ];
}

// Handle search
function handleSearch(event) {
    event.preventDefault();
    
    const categoria = document.getElementById('categoria').value;
    const localizacao = document.getElementById('localizacao').value;
    const palavraChave = document.getElementById('palavraChave').value;
    
    // Filter ads based on search criteria
    searchResults = ads.filter(ad => {
        let matches = true;
        
        if (categoria && ad.category !== categoria) {
            matches = false;
        }
        
        if (localizacao && !ad.location.toLowerCase().includes(localizacao.toLowerCase())) {
            matches = false;
        }
        
        if (palavraChave) {
            const searchTerm = palavraChave.toLowerCase();
            if (!ad.title.toLowerCase().includes(searchTerm) && 
                !ad.description.toLowerCase().includes(searchTerm)) {
                matches = false;
            }
        }
        
        return matches;
    });
    
    displaySearchResults();
}

// Display search results
function displaySearchResults() {
    const resultsSection = document.getElementById('resultsSection');
    const resultsContainer = document.getElementById('resultsContainer');
    
    if (!resultsSection || !resultsContainer) return;
    
    resultsSection.style.display = 'block';
    resultsContainer.innerHTML = '';
    
    if (searchResults.length === 0) {
        resultsContainer.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="fas fa-search fa-3x text-muted mb-3"></i>
                <h4 class="text-muted">Nenhum resultado encontrado</h4>
                <p class="text-muted">Tente ajustar os filtros de busca.</p>
            </div>
        `;
        return;
    }
    
    searchResults.forEach(ad => {
        const adCard = createAdCard(ad);
        resultsContainer.appendChild(adCard);
    });
}

// Create ad card
function createAdCard(ad) {
    const col = document.createElement('div');
    col.className = 'col-lg-6 mb-4';
    
    col.innerHTML = `
        <div class="card h-100">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <h5 class="card-title">${ad.title}</h5>
                    <span class="badge bg-primary">${getCategoryName(ad.category)}</span>
                </div>
                <p class="card-text">${ad.description.substring(0, 100)}...</p>
                <div class="mb-2">
                    <small class="text-muted">
                        <i class="fas fa-user me-1"></i>${ad.provider} |
                        <i class="fas fa-map-marker-alt me-1"></i>${ad.location} |
                        <i class="fas fa-eye me-1"></i>${ad.views} visualizações
                    </small>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    <strong class="text-success">${ad.price}</strong>
                    <button class="btn btn-primary" onclick="openContactModal(${ad.id})">
                        <i class="fas fa-envelope me-1"></i>Entrar em Contato
                    </button>
                </div>
            </div>
        </div>
    `;
    
    return col;
}

// Get category name by key
function getCategoryName(categoryKey) {
    const categoryMap = {
        'reparos': 'Reparos',
        'transporte': 'Transporte',
        'culinaria': 'Culinária',
        'limpeza': 'Limpeza',
        'jardinagem': 'Jardinagem',
        'beleza': 'Beleza',
        'educacao': 'Educação',
        'tecnologia': 'Tecnologia'
    };
    return categoryMap[categoryKey] || categoryKey;
}

// Open contact modal
function openContactModal(adId) {
    const ad = ads.find(a => a.id === adId);
    if (!ad) return;
    
    const modal = new bootstrap.Modal(document.getElementById('contactModal'));
    document.querySelector('#contactModal .modal-title').textContent = `Contatar: ${ad.provider}`;
    modal.show();
}

// Handle login
function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;
    
    // Mock login validation
    if (email && senha) {
        if (email === 'admin@servicosja.com') {
            currentUser = { type: 'admin', name: 'Administrador' };
            window.location.href = 'admin-dashboard.html';
        } else {
            currentUser = { type: 'prestador', name: 'João Silva', email: email };
            window.location.href = 'prestador-dashboard.html';
        }
        
        // Store user session
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        
        showAlert('Login realizado com sucesso!', 'success');
    } else {
        showAlert('Por favor, preencha todos os campos.', 'error');
    }
}

// Demo login
function loginDemo(userType) {
    if (userType === 'admin') {
        document.getElementById('loginEmail').value = 'admin@servicosja.com';
        document.getElementById('loginSenha').value = 'admin123';
    } else {
        document.getElementById('loginEmail').value = 'joao.silva@email.com';
        document.getElementById('loginSenha').value = 'prestador123';
    }
}

// Handle registration
function handleRegistration(event) {
    event.preventDefault();
    
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;
    
    // Basic validation
    if (senha !== confirmarSenha) {
        showAlert('As senhas não coincidem.', 'error');
        return;
    }
    
    if (!document.getElementById('aceitarTermos').checked) {
        showAlert('Você deve aceitar os termos de uso.', 'error');
        return;
    }
    
    // Mock registration
    showAlert('Cadastro realizado com sucesso! Você já pode fazer login.', 'success');
    setTimeout(() => {
        window.location.href = 'login.html';
    }, 2000);
}

// Handle ad submission
function handleAdSubmission(event) {
    event.preventDefault();
    
    const titulo = document.getElementById('titulo').value;
    const categoria = document.getElementById('categoria').value;
    const descricao = document.getElementById('descricao').value;
    
    if (!titulo || !categoria || !descricao) {
        showAlert('Por favor, preencha todos os campos obrigatórios.', 'error');
        return;
    }
    
    // Mock ad creation
    const newAd = {
        id: ads.length + 1,
        title: titulo,
        category: categoria,
        description: descricao,
        provider: 'João Silva',
        status: 'ativo',
        views: 0,
        date: new Date().toISOString().split('T')[0]
    };
    
    ads.push(newAd);
    
    showAlert('Anúncio criado com sucesso!', 'success');
    setTimeout(() => {
        window.location.href = 'meus-anuncios.html';
    }, 2000);
}

// Handle contact submission
function handleContactSubmission(event) {
    event.preventDefault();
    showAlert('Mensagem enviada com sucesso! O prestador entrará em contato em breve.', 'success');
    const modal = bootstrap.Modal.getInstance(document.getElementById('contactModal'));
    modal.hide();
}

// Send message
function sendMessage() {
    const form = document.querySelector('#contactModal form');
    if (form.checkValidity()) {
        handleContactSubmission({ preventDefault: () => {} });
    } else {
        showAlert('Por favor, preencha todos os campos obrigatórios.', 'error');
    }
}

// Toggle password visibility
function togglePassword() {
    const passwordInput = document.getElementById('loginSenha');
    const toggleIcon = document.getElementById('toggleIcon');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.className = 'fas fa-eye-slash';
    } else {
        passwordInput.type = 'password';
        toggleIcon.className = 'fas fa-eye';
    }
}

// Check user session
function checkUserSession() {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
        currentUser = JSON.parse(storedUser);
    }
}

// Setup form validation
function setupFormValidation() {
    // Add Bootstrap validation classes
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}

// Setup image previews
function setupImagePreviews() {
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function(event) {
            const file = event.target.files[0];
            const previewId = input.id.replace('foto', 'preview');
            const preview = document.getElementById(previewId);
            
            if (file && preview) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.style.display = 'block';
                    preview.querySelector('img').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    });
}

// CEP lookup
function lookupCEP() {
    const cep = document.getElementById('cep').value.replace(/\D/g, '');
    
    if (cep.length === 8) {
        fetch(`https://viacep.com.br/ws/${cep}/json/`)
            .then(response => response.json())
            .then(data => {
                if (!data.erro) {
                    document.getElementById('rua').value = data.logradouro;
                    document.getElementById('bairro').value = data.bairro;
                    document.getElementById('cidade').value = data.localidade;
                    document.getElementById('estado').value = data.uf;
                }
            })
            .catch(error => {
                console.log('Erro ao buscar CEP:', error);
            });
    }
}

// Apply filters
function applyFilters() {
    // This would filter the ads list based on selected filters
    console.log('Aplicando filtros...');
}

// Message functions
function replyMessage(messageId) {
    showAlert('Funcionalidade de resposta será implementada no backend.', 'info');
}

function deleteMessage(messageId) {
    if (confirm('Tem certeza que deseja excluir esta mensagem?')) {
        messages = messages.filter(m => m.id !== messageId);
        showAlert('Mensagem excluída com sucesso!', 'success');
        // Refresh the page or update the message list
        location.reload();
    }
}

// Ad management functions
function editAd(adId) {
    window.location.href = `cadastro-anuncio.html?edit=${adId}`;
}

function toggleAdStatus(adId) {
    const ad = ads.find(a => a.id === adId);
    if (ad) {
        ad.status = ad.status === 'ativo' ? 'pausado' : 'ativo';
        showAlert(`Anúncio ${ad.status === 'ativo' ? 'ativado' : 'pausado'} com sucesso!`, 'success');
        location.reload();
    }
}

function deleteAd(adId) {
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
    
    // Store the ad ID for confirmation
    window.adToDelete = adId;
}

function confirmDelete() {
    if (window.adToDelete) {
        ads = ads.filter(a => a.id !== window.adToDelete);
        showAlert('Anúncio excluído com sucesso!', 'success');
        const modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
        modal.hide();
        location.reload();
    }
}

// Profile functions
function saveProfile() {
    showAlert('Perfil atualizado com sucesso!', 'success');
    const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
    modal.hide();
}

function loadMoreMessages() {
    showAlert('Não há mais mensagens para carregar.', 'info');
}

// Admin functions
function editCategory(categoryId) {
    showAlert('Funcionalidade de edição será implementada.', 'info');
}

function deleteCategory(categoryId) {
    if (confirm('Tem certeza que deseja excluir esta categoria?')) {
        showAlert('Categoria excluída com sucesso!', 'success');
        location.reload();
    }
}

function saveCategory() {
    showAlert('Categoria salva com sucesso!', 'success');
    const modal = bootstrap.Modal.getInstance(document.getElementById('addCategoryModal'));
    modal.hide();
    location.reload();
}

function viewAd(adId) {
    showAlert('Visualização detalhada será implementada.', 'info');
}

function viewProvider(providerId) {
    showAlert('Visualização do prestador será implementada.', 'info');
}

function suspendProvider(providerId) {
    if (confirm('Tem certeza que deseja suspender este prestador?')) {
        showAlert('Prestador suspenso com sucesso!', 'warning');
        location.reload();
    }
}

// Ad creation functions
function saveAsDraft() {
    showAlert('Rascunho salvo com sucesso!', 'success');
}

function publishAd() {
    showAlert('Anúncio publicado com sucesso!', 'success');
    setTimeout(() => {
        window.location.href = 'meus-anuncios.html';
    }, 2000);
}

// Password reset
function sendPasswordReset() {
    showAlert('Instruções de recuperação enviadas para seu email!', 'success');
    const modal = bootstrap.Modal.getInstance(document.getElementById('forgotPasswordModal'));
    modal.hide();
}

// Utility functions
function showAlert(message, type = 'info') {
    // Create alert element
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);
}

// Format phone number
function formatPhone(input) {
    let value = input.value.replace(/\D/g, '');
    value = value.replace(/(\d{2})(\d)/, '($1) $2');
    value = value.replace(/(\d{5})(\d)/, '$1-$2');
    input.value = value;
}

// Format CPF
function formatCPF(input) {
    let value = input.value.replace(/\D/g, '');
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    input.value = value;
}

// Format CEP
function formatCEP(input) {
    let value = input.value.replace(/\D/g, '');
    value = value.replace(/(\d{5})(\d)/, '$1-$2');
    input.value = value;
}

// Initialize masks on page load
document.addEventListener('DOMContentLoaded', function() {
    // Phone masks
    const phoneInputs = document.querySelectorAll('input[type="tel"]');
    phoneInputs.forEach(input => {
        input.addEventListener('input', () => formatPhone(input));
    });
    
    // CPF mask
    const cpfInput = document.getElementById('cpf');
    if (cpfInput) {
        cpfInput.addEventListener('input', () => formatCPF(cpfInput));
    }
    
    // CEP mask
    const cepInput = document.getElementById('cep');
    if (cepInput) {
        cepInput.addEventListener('input', () => formatCEP(cepInput));
    }
});