// 获取按钮和隐藏按钮容器
const moreBtn = document.getElementById('moreBtn');
const hiddenButtons = document.getElementById('hiddenButtons');

// 监听“更多”按钮的点击事件
moreBtn.addEventListener('click', function() {
    // 切换隐藏按钮的显示状态
    if (hiddenButtons.style.display === 'none') {
        hiddenButtons.style.display = 'block';
    } else {
        hiddenButtons.style.display = 'none';
    }
});
