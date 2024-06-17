document.addEventListener('DOMContentLoaded', function () {
    const list = document.getElementById('list');

    function render(listContent) {
        let listHtml = '';
        listContent.forEach(function (item, index) {
            listHtml += `
                <div class='listItem' data-index="${index}">
                    <button class="deleteButton">Delete</button> <!-- 添加 delete 按钮 -->
                    <h2>標題: ${item.title}</h2>
                    <p>日期: ${item.date}</p>
                    <p>內容: ${item.note}</p>
                    <p>上次編輯於: ${item.time}</p>
                </div>
            `;
        });
        list.innerHTML = listHtml;

        // 添加事件监听器
        document.querySelectorAll('.deleteButton').forEach(button => {
            button.addEventListener('click', () => {
                const index = button.parentElement.getAttribute('data-index');
                // 执行删除操作，这里你可以根据需求进行具体操作
                console.log('Delete item at index:', index);
                // 可以将删除逻辑写在这里，例如：从 localStorage 中删除对应的数据项，然后重新渲染列表
            });
        });
    }

    if (localStorage.listContent) {
        const listContent = JSON.parse(localStorage.listContent);
        render(listContent);
    }
});
