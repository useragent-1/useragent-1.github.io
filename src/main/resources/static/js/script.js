// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化粒子效果
    initParticles();
    
    // 初始化表单动画
    initFormAnimations();
    
    // 初始化仪表盘效果
    initDashboard();
    
    // 初始化消息提示
    initAlerts();
});

// 创建浮动粒子效果
function initParticles() {
    const container = document.querySelector('.floating-particles');
    if (!container) return;
    
    // 清除现有粒子
    container.innerHTML = '';
    
    // 创建新粒子
    for (let i = 0; i < 4; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        container.appendChild(particle);
    }
    
    // 添加额外的小粒子
    for (let i = 0; i < 20; i++) {
        const smallParticle = document.createElement('div');
        smallParticle.className = 'particle small-particle';
        smallParticle.style.width = Math.random() * 10 + 5 + 'px';
        smallParticle.style.height = smallParticle.style.width;
        smallParticle.style.background = `radial-gradient(circle, rgba(255, 255, 255, 0.8) 0%, transparent 70%)`;
        smallParticle.style.top = Math.random() * 100 + '%';
        smallParticle.style.left = Math.random() * 100 + '%';
        smallParticle.style.animationDuration = Math.random() * 10 + 5 + 's';
        smallParticle.style.animationDelay = Math.random() * 5 + 's';
        container.appendChild(smallParticle);
    }
}

// 表单输入动画效果
function initFormAnimations() {
    const formControls = document.querySelectorAll('.form-control');
    
    formControls.forEach(input => {
        // 输入框获得焦点时添加动画
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        // 输入框失去焦点时移除动画
        input.addEventListener('blur', function() {
            this.parentElement.classList.remove('focused');
        });
        
        // 如果输入框有值，添加filled类
        input.addEventListener('input', function() {
            if (this.value.trim() !== '') {
                this.classList.add('filled');
            } else {
                this.classList.remove('filled');
            }
        });
        
        // 初始检查是否有值
        if (input.value.trim() !== '') {
            input.classList.add('filled');
        }
    });
    
    // 按钮悬停效果
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(btn => {
        btn.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-3px)';
            this.style.boxShadow = '0 7px 14px rgba(10, 132, 255, 0.3)';
        });
        
        btn.addEventListener('mouseleave', function() {
            this.style.transform = '';
            this.style.boxShadow = '';
        });
    });
}

// 仪表盘相关效果
function initDashboard() {
    // 侧边栏切换
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.querySelector('.sidebar').classList.toggle('collapsed');
            document.querySelector('.main-content').classList.toggle('expanded');
        });
    }
    
    // 高亮当前活动菜单项
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('.sidebar-menu a');
    
    menuLinks.forEach(link => {
        if (currentPath.includes(link.getAttribute('href'))) {
            link.classList.add('active');
        }
    });
    
    // 添加卡片悬停效果
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 8px 30px rgba(0, 0, 0, 0.3)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = '';
            this.style.boxShadow = '';
        });
    });
}

// 消息提示效果
function initAlerts() {
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(alert => {
        // 添加关闭按钮
        const closeBtn = document.createElement('button');
        closeBtn.className = 'close-alert';
        closeBtn.innerHTML = '&times;';
        closeBtn.style.position = 'absolute';
        closeBtn.style.right = '10px';
        closeBtn.style.top = '10px';
        closeBtn.style.background = 'none';
        closeBtn.style.border = 'none';
        closeBtn.style.color = 'inherit';
        closeBtn.style.fontSize = '1.2rem';
        closeBtn.style.cursor = 'pointer';
        
        alert.style.position = 'relative';
        alert.appendChild(closeBtn);
        
        // 点击关闭按钮移除提示
        closeBtn.addEventListener('click', function() {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-20px)';
            alert.style.transition = 'all 0.3s ease';
            
            setTimeout(() => {
                alert.remove();
            }, 300);
        });
        
        // 自动消失
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-20px)';
            alert.style.transition = 'all 0.3s ease';
            
            setTimeout(() => {
                alert.remove();
            }, 300);
        }, 5000);
    });
}

// 添加密码强度检查
function checkPasswordStrength(password) {
    const strengthMeter = document.getElementById('password-strength');
    if (!strengthMeter) return;
    
    // 密码强度评分
    let score = 0;
    
    // 长度检查
    if (password.length >= 8) score += 1;
    if (password.length >= 12) score += 1;
    
    // 复杂度检查
    if (/[A-Z]/.test(password)) score += 1; // 大写字母
    if (/[a-z]/.test(password)) score += 1; // 小写字母
    if (/[0-9]/.test(password)) score += 1; // 数字
    if (/[^A-Za-z0-9]/.test(password)) score += 1; // 特殊字符
    
    // 更新强度显示
    let strengthText = '';
    let strengthClass = '';
    
    if (password.length === 0) {
        strengthText = '';
        strengthClass = '';
    } else if (score < 3) {
        strengthText = '弱';
        strengthClass = 'weak';
    } else if (score < 5) {
        strengthText = '中';
        strengthClass = 'medium';
    } else {
        strengthText = '强';
        strengthClass = 'strong';
    }
    
    strengthMeter.textContent = strengthText;
    strengthMeter.className = strengthClass;
}

// 表单验证
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;
    
    let isValid = true;
    const inputs = form.querySelectorAll('input[required]');
    
    inputs.forEach(input => {
        if (input.value.trim() === '') {
            isValid = false;
            input.classList.add('is-invalid');
            
            // 添加错误提示
            let errorMsg = input.nextElementSibling;
            if (!errorMsg || !errorMsg.classList.contains('error-message')) {
                errorMsg = document.createElement('div');
                errorMsg.className = 'error-message';
                errorMsg.style.color = 'var(--danger-color)';
                errorMsg.style.fontSize = '0.8rem';
                errorMsg.style.marginTop = '5px';
                input.parentNode.insertBefore(errorMsg, input.nextSibling);
            }
            errorMsg.textContent = '此字段不能为空';
        } else {
            input.classList.remove('is-invalid');
            
            // 移除错误提示
            const errorMsg = input.nextElementSibling;
            if (errorMsg && errorMsg.classList.contains('error-message')) {
                errorMsg.remove();
            }
        }
    });
    
    // 密码匹配验证
    const password = form.querySelector('input[name="password"]');
    const confirmPassword = form.querySelector('input[name="confirmPassword"]');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
        isValid = false;
        confirmPassword.classList.add('is-invalid');
        
        // 添加错误提示
        let errorMsg = confirmPassword.nextElementSibling;
        if (!errorMsg || !errorMsg.classList.contains('error-message')) {
            errorMsg = document.createElement('div');
            errorMsg.className = 'error-message';
            errorMsg.style.color = 'var(--danger-color)';
            errorMsg.style.fontSize = '0.8rem';
            errorMsg.style.marginTop = '5px';
            confirmPassword.parentNode.insertBefore(errorMsg, confirmPassword.nextSibling);
        }
        errorMsg.textContent = '两次密码不一致';
    }
    
    return isValid;
}

// 添加表单提交验证
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this.id)) {
                e.preventDefault();
            }
        });
    });
    
    // 密码输入监听
    const passwordInputs = document.querySelectorAll('input[type="password"]');
    passwordInputs.forEach(input => {
        if (input.name === 'password' || input.name === 'newPassword') {
            input.addEventListener('input', function() {
                checkPasswordStrength(this.value);
            });
        }
    });
});