document.addEventListener('DOMContentLoaded', function () {
    const favoriteButton = document.querySelectorAll('.favorite-button');

    favoriteButton.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();

            const storeId = this.dataset.storeId;

            fetch(`/storeinfo/${storeId}/favorite`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                }
            }).then(response => response.json())
              .then(data => {
                  // モーダルウィンドウを表示
                  if(data.status === 'success') {
                      const favoriteModal = new bootstrap.Modal(document.getElementById('favoriteModal'));
                      favoriteModal.show();
                  } else {
                      alert('追加できませんでした。もう一度試してください。');
                  }
              });
        });
    });
});
