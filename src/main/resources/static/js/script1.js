// Obtén una referencia al campo de búsqueda
const searchInput = document.getElementById('buscarNoticias');

// Obtén todas los elementos que deseas filtrar (noticias y usuarios)
const elementos = document.querySelectorAll('.noticia, .usuario');

// Agrega un evento de escucha al campo de búsqueda
searchInput.addEventListener('input', function() {
    const searchTerm = searchInput.value.toLowerCase(); // Obtén el término de búsqueda en minúsculas

    // Itera sobre los elementos y muéstralos u ocúltalos según coincidan con el término de búsqueda
    elementos.forEach(elemento => {
        const titulo = elemento.querySelector('.titulo').textContent.toLowerCase();
     //   const rol = elemento.querySelector('.rol').textContent.toLowerCase();
     //   const email = elemento.querySelector('.email').textContent.toLowerCase();

        if (searchTerm === '' || titulo.includes(searchTerm)) {
            elemento.style.display = 'table-row';
        } else {
            elemento.style.display = 'none';
        }
    });
});