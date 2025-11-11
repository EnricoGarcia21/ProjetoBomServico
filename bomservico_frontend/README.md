# BomServiço - Frontend

Modern, responsive frontend for the BomServiço service classifieds platform.

## 🎨 Design Features

- **Modern UI/UX** - Clean, professional design with purple/pink gradient theme
- **Glassmorphism Effects** - Beautiful frosted glass card designs
- **Smooth Animations** - Fade-in, slide-up, and hover effects
- **Fully Responsive** - Mobile-first design approach
- **Toast Notifications** - User-friendly feedback system
- **Loading States** - Spinners and overlays for better UX

## 📁 Project Structure

```
bomservico_frontend/
├── css/
│   └── style.css          # Complete design system with custom CSS
├── js/
│   ├── api.js             # API communication utilities
│   ├── auth.js            # JWT authentication & authorization
│   └── utils.js           # Helper functions & utilities
├── index.html             # Home page with search and browse
├── login.html             # Login page
├── cadastro-prestador.html # Provider registration
├── anuncio-detalhes.html  # Ad details and contact form
├── prestador-dashboard.html # Provider dashboard with messages
├── meus-anuncios.html     # Provider's ads management
├── cadastro-anuncio.html  # Create/edit ad form
└── admin-dashboard.html   # Admin panel for moderation
```

## 🚀 Quick Start

### 1. Prerequisites

- Backend API running on `http://localhost:8080`
- Modern web browser
- Live Server or similar for development

### 2. Configuration

Update the API base URL in `js/api.js` if needed:

```javascript
const API_BASE_URL = 'http://localhost:8080/apis';
```

### 3. Run Development Server

**Option 1: VS Code Live Server**
- Install "Live Server" extension
- Right-click `index.html` → "Open with Live Server"

**Option 2: Python HTTP Server**
```bash
cd bomservico_frontend
python -m http.server 8000
```

**Option 3: Node.js http-server**
```bash
npm install -g http-server
http-server bomservico_frontend
```

Then open: `http://localhost:8000` (or your configured port)

## 📄 Pages Overview

### Public Pages

**index.html** - Home Page
- Hero section with search
- Category grid
- Featured ads
- CTA for provider registration

**login.html** - Authentication
- Login form
- JWT token handling
- Role-based redirects

**cadastro-prestador.html** - Registration
- Provider signup form
- Form validation
- Password requirements

**anuncio-detalhes.html** - Ad Details
- Full ad information
- Category badges
- Contact form for interested users

### Provider Pages (Requires Login - nivel=0)

**prestador-dashboard.html**
- Statistics cards
- Received messages inbox
- Quick actions

**meus-anuncios.html**
- List of provider's ads
- Edit/delete actions
- Create new ad button

**cadastro-anuncio.html**
- Create/edit ad form
- Category selection
- Working hours configuration

### Admin Pages (Requires Login - nivel=1)

**admin-dashboard.html**
- Category management (CRUD)
- Ad moderation
- Delete inappropriate content

## 🔐 Authentication Flow

1. **Registration** (`cadastro-prestador.html`)
   - User fills registration form
   - POST `/public/add-user`
   - Redirect to login

2. **Login** (`login.html`)
   - User enters credentials
   - POST `/public/login`
   - Receive JWT token
   - Store token in localStorage
   - Redirect based on role:
     - Admin (nivel=1) → admin-dashboard.html
     - Provider (nivel=0) → prestador-dashboard.html

3. **Protected Routes**
   - Auth.requireAuth() - Any authenticated user
   - Auth.requirePrestador() - Only providers
   - Auth.requireAdmin() - Only admins
   - Auto-redirect if unauthorized

4. **Logout**
   - Click "Sair" in navbar
   - Clear localStorage
   - Redirect to index.html

## 🎨 Design System

### Colors

```css
--primary: #6366f1 (Indigo)
--secondary: #ec4899 (Pink)
--success: #10b981 (Green)
--error: #ef4444 (Red)
--warning: #f59e0b (Orange)
```

### Typography

- **Display**: Poppins (headings)
- **Body**: Inter (content)
- Fully responsive font sizes

### Components

- **Cards** - `.card`, `.card-glass`
- **Buttons** - `.btn-primary`, `.btn-secondary`, `.btn-outline`
- **Forms** - `.form-control`, `.form-label`, `.form-group`
- **Badges** - `.badge`, `.badge-primary`
- **Alerts** - `.alert`, `.alert-success`, `.alert-error`
- **Modals** - `.modal`, `.modal-content`
- **Grid** - `.grid`, `.grid-cols-2`, `.grid-cols-3`

### Animations

- `fade-in` - Fade in effect
- `slide-up` - Slide up from bottom
- Toast notifications - Slide in from right

## 🛠️ Utilities (utils.js)

```javascript
showToast(message, type)       // Show notification
showLoading() / hideLoading()  // Loading overlay
showModal(id) / hideModal(id)  // Modal dialogs
confirm(message)               // Confirmation dialog
formatDate(date)               // Format to pt-BR
formatPhone(phone)             // Format phone number
formatCPF(cpf)                 // Format CPF
isValidEmail(email)            // Email validation
truncate(text, length)         // Truncate text
debounce(func, wait)           // Debounce function
```

## 🔌 API Integration (api.js)

### Public Endpoints
```javascript
API.login(credentials)
API.register(userData)
API.searchAnuncios(keyword)
API.getAnuncioPublic(id)
API.sendInteresse(anuncioId, data)
```

### Provider Endpoints
```javascript
API.getCategorias()
API.getMyAnuncios()
API.createAnuncio(data)
API.updateAnuncio(data)
API.deleteAnuncio(id)
API.getProfile()
API.updateProfile(data)
API.getMensagens()
API.deleteMensagem(id)
API.uploadFoto(anuncioId, file)
API.deleteFoto(fotoId)
```

### Admin Endpoints
```javascript
API.getAllCategorias()
API.createCategoria(data)
API.updateCategoria(data)
API.deleteCategoria(id)
API.getAllAnunciosAdmin()
API.deleteAnuncioAdmin(id)
```

## 🎯 Features Implemented

✅ **User Management**
- Registration with validation
- JWT authentication
- Role-based access control
- Profile management

✅ **Ad Management**
- Create/edit/delete ads
- Category assignment
- Working hours
- Photo upload (up to 3)
- Search and filter

✅ **Messaging System**
- Interest messages
- Contact forms
- Message inbox
- Delete messages

✅ **Admin Panel**
- Category CRUD
- Ad moderation
- Delete inappropriate content

✅ **UI/UX**
- Responsive design
- Toast notifications
- Loading states
- Empty states
- Form validation
- Modal dialogs
- Smooth animations

## 📱 Responsive Breakpoints

- **Mobile**: < 768px (Single column)
- **Tablet**: 768px - 1024px (Adapted grids)
- **Desktop**: > 1024px (Full layout)

## 🔒 Security

- JWT tokens stored in localStorage
- Auto-logout on 401 responses
- CORS enabled for API calls
- Form validation
- XSS protection via proper escaping

## 🎨 Customization

### Change Theme Colors

Edit `css/style.css`:

```css
:root {
    --primary: #YOUR_COLOR;
    --secondary: #YOUR_COLOR;
}
```

### Change API URL

Edit `js/api.js`:

```javascript
const API_BASE_URL = 'https://your-api.com/apis';
```

### Add New Pages

1. Create HTML file
2. Include CSS and JS files
3. Add navbar with `Auth.updateNavbar()`
4. Add authentication check if needed

## 🐛 Troubleshooting

**Problem**: Ads not loading
- **Solution**: Check if backend is running on port 8080
- **Solution**: Check browser console for CORS errors

**Problem**: Login fails
- **Solution**: Ensure users are registered via API (BCrypt hashed passwords)
- **Solution**: Check JWT token in localStorage

**Problem**: 401 Unauthorized
- **Solution**: Token expired, login again
- **Solution**: Check if Authorization header is sent

**Problem**: Images not loading
- **Solution**: Ensure `uploads/fotos` directory exists in backend
- **Solution**: Check file paths in database

## 📝 Future Enhancements

- [ ] Password reset functionality
- [ ] Email notifications
- [ ] Real-time chat
- [ ] Review/rating system
- [ ] Advanced search filters
- [ ] Map integration
- [ ] Payment integration
- [ ] Social login (Google, Facebook)
- [ ] Progressive Web App (PWA)
- [ ] Dark mode toggle

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

© 2025 BomServiço. All rights reserved.

---

**Built with ❤️ using vanilla HTML, CSS, and JavaScript**
