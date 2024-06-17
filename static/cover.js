// 更新时间和日期显示
function updateTimeDate() {
  const now = new Date();
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  const timeString = `Time: ${hours}:${minutes}:${seconds}`;

  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const dateString = `Date: ${year}-${month}-${day}`;

  document.getElementById('time').textContent = timeString;
  document.getElementById('date').textContent = dateString;
}

document.addEventListener("DOMContentLoaded", function() {
  const searchDropdown = document.getElementById('search-dropdown');
  const searchInput = document.getElementById('search');
  const placeholderText = "輸入關鍵詞";

  searchDropdown.addEventListener('change', function() {
    if (this.value === 'date') {
      searchInput.type = 'date';
      searchInput.placeholder = '';
    } else {
      searchInput.type = 'text';
      searchInput.placeholder = placeholderText;
    }
  });

  document.getElementById('clickableDiv').addEventListener('click', function() {
    window.location.href = 'notepage.html'; // 替换为你希望的URL
  });

  document.getElementById('clickToAddNote').addEventListener('click', function() {
    localStorage.removeItem('editIndex');
    window.location.href = 'noteadd.html';
  });

  document.getElementById('clickToAddCalendar').addEventListener('click', function() {
    window.location.href = 'calendar2.html'; // 替换为你希望的URL
  });

  document.getElementById('clickToLottery').addEventListener('click', function() {
    localStorage.removeItem('editIndex');
    window.location.href = 'lottery.html';
  });


  const containerWidth = document.querySelector('.container').clientWidth;
  const desiredHorizontalOffset = containerWidth / 2;
  document.querySelector('.container').style.left = `-${desiredHorizontalOffset}px`;

  setInterval(updateTimeDate, 1000);
  updateTimeDate(); // 初始化时立即显示时间和日期
});
