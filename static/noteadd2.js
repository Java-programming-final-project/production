window.addEventListener('load', function () {
    const saveBtn = document.getElementById('save');
    const deleteBtn = document.getElementById('delete');
    const deleteAllBtn = document.getElementById('deleteAll');
    const viewNotesBtn = document.getElementById('viewNotes');
    const date = document.getElementById('date');
    const note = document.getElementById('note');
    const title = document.getElementById('title');

    let editIndex = localStorage.getItem('editIndex') !== null ? parseInt(localStorage.getItem('editIndex')) : null;
    let listContentOfCalendar = localStorage.listContentOfCalendar ? JSON.parse(localStorage.listContentOfCalendar) : [];

    // 如果editIndex不为空，加载相应的笔记内容
    if (editIndex !== null) {
        const selectedItem = listContentOfCalendar[editIndex];
        title.value = selectedItem.title;
        date.value = selectedItem.date;
        note.value = selectedItem.note;
    }

    function saveToStorage() {
        localStorage.listContentOfCalendar = JSON.stringify(listContentOfCalendar);
    }

    function getCurrentDate() {
        const now = new Date();
        return now.toISOString().split('T')[0];
    }

    function getCurrentTime() {
        const now = new Date();
        return now.toLocaleTimeString();
    }

    saveBtn.addEventListener('click', function () {
        const currentDate = date.value || getCurrentDate();
        const currentTime = getCurrentTime();
        const noteData = {
            title: title.value,
            date: currentDate,
            note: note.value,
            time: currentTime
        };

        if (editIndex !== null) {
            listContent[editIndex] = noteData; // 更新现有笔记
            editIndex = null;
            localStorage.removeItem('editIndex');
        } else {
            listContentOfCalendar.unshift(noteData); // 新增笔记
        }

        saveToStorage();
        title.value = '';
        date.value = '';
        note.value = '';
        window.location.href = 'calendar2.html'; // 保存后返回笔记列表页面
    });

    deleteBtn.addEventListener('click', function () {
        if (editIndex !== null) {
            listContent.splice(editIndex, 1); // 删除对应索引的笔记
            editIndex = null;
            localStorage.removeItem('editIndex');
        } else {
            listContentOfCalendar.shift(); // 如果没有editIndex，删除第一个笔记
        }
        saveToStorage();
        title.value = '';
        date.value = '';
        note.value = '';
        window.location.href = 'calendar2.html'; // 删除后返回笔记列表页面
    });

    deleteAllBtn.addEventListener('click', function () {
        if (window.confirm('Are you sure to clear ALL the notes?')) {
            listContent = [];
            saveToStorage();
            title.value = '';
            date.value = '';
            note.value = '';
            window.location.href = 'calendar2.html'; // 删除全部后返回笔记列表页面
        }
    });

    viewNotesBtn.addEventListener('click', function () {
        window.location.href = 'calendar2.html'; // 点击查看笔记按钮时返回笔记列表页面
    });

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

});