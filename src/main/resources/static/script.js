let currentPage = 1;
let resultsPerPage = 10;
let totalRecords = 0;

let sortBy = '';
let sortOrder = '';
let daysFilter = 10;  

const resultsInfo = document.getElementById('results-info');
const tableBody = document.getElementById('cve-table-body');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');
const resultsSelect = document.getElementById('results-per-page');
const pageNumbersContainer = document.getElementById('page-numbers');
const totalRecord = document.getElementById("totalrecords");

const iconPublished = document.getElementById("icon-published");
const iconModified = document.getElementById("icon-modified");

async function loadData() {
  const offset = (currentPage - 1) * resultsPerPage;
  
  const url = new URL('http://localhost:3000/api/cves/list');
  url.searchParams.set('limit', resultsPerPage);
  url.searchParams.set('offset', offset);

  if (daysFilter) {
   
    if (sortBy && sortOrder) {
      url.searchParams.set('sortBy', sortBy);
      url.searchParams.set('order', sortOrder);
    }
  }

  try {
    const res = await fetch(url.toString());
    const json = await res.json();
    console.log("Raw API response:", json.cves);
    console.log("First CVE object:", json.cves?.[0]);

    totalRecords = json.total;
    totalRecord.textContent = "Total Records : " + totalRecords;

    const startRecord = totalRecords === 0 ? 0 : offset + 1;
    const endRecord = Math.min(currentPage * resultsPerPage, totalRecords);
    resultsInfo.textContent = `${startRecord}-${endRecord} of ${totalRecords} records`;

    tableBody.innerHTML = '';
    (json.cves || []).forEach(cve => {
      const tr = document.createElement('tr');

      tr.innerHTML = `
  <td>${cve.cve_id}</td>
  <td>${cve.sourceIdentifier ?? 'N/A'}</td>
  <td>${formatDate(cve.published)}</td>
  <td>${formatDate(cve.last_modified)}</td>
  <td>${cve.vulnStatus ?? 'N/A'}</td>
`;



      tr.style.cursor = 'pointer';
      tr.addEventListener('click', () => {
        window.location.href = `Page/Details.html?cveId=${cve.cve_id}`;
      });

      tableBody.appendChild(tr);
    });

    renderPaginationButtons();
  } catch (error) {
    console.error('Error loading data:', error);
  }
}

function toggleSort(column) {
  if (!daysFilter) {
    alert("Sorting is available only when filtering by last N days.");
    return;
  }

  if (sortBy === column) {
    sortOrder = sortOrder === 'asc' ? 'desc' : 'asc';
  } else {
    sortBy = column;
    sortOrder = 'asc';
  }

  iconPublished.textContent = "⬍";
  iconModified.textContent = "⬍";

  const arrow = sortOrder === 'asc' ? '⬆️' : '⬇️';
  if (column === 'published') iconPublished.textContent = arrow;
  if (column === 'last_modified') iconModified.textContent = arrow;

  currentPage = 1;
  loadData();
}

document.getElementById("sort-published").addEventListener("click", () => toggleSort("published"));
document.getElementById("sort-modified").addEventListener("click", () => toggleSort("last_modified"));

prevBtn.onclick = () => {
  if (currentPage > 1) {
    currentPage--;
    loadData();
  }
};

nextBtn.onclick = () => {
  const totalPages = Math.ceil(totalRecords / resultsPerPage);
  if (currentPage < totalPages) {
    currentPage++;
    loadData();
  }
};

resultsSelect.onchange = () => {
  resultsPerPage = +resultsSelect.value;
  currentPage = 1;
  loadData();
};

function renderPaginationButtons() {
  const totalPages = Math.ceil(totalRecords / resultsPerPage);
  pageNumbersContainer.innerHTML = '';

  prevBtn.disabled = currentPage === 1;
  nextBtn.disabled = currentPage === totalPages || totalPages === 0;

  const maxPageButtons = 5;
  const half = Math.floor(maxPageButtons / 2);

  let startPage = Math.max(1, currentPage - half);
  let endPage = startPage + maxPageButtons - 1;

  if (endPage > totalPages) {
    endPage = totalPages;
    startPage = Math.max(1, endPage - maxPageButtons + 1);
  }

  for (let i = startPage; i <= endPage; i++) {
    const btn = document.createElement('button');
    btn.textContent = i;
    if (i === currentPage) {
      btn.classList.add('active');
      btn.disabled = true;
    }
    btn.addEventListener('click', () => {
      if (currentPage !== i) {
        currentPage = i;
        loadData();
      }
    });
    pageNumbersContainer.appendChild(btn);
  }
}

function formatDate(dateString) {
  if (!dateString) return 'N/A';

  const date = new Date(dateString);
  if (isNaN(date.getTime())) return 'N/A';

  const day = date.getDate().toString().padStart(2, '0');
  const month = date.toLocaleString('en-GB', { month: 'short' }).toUpperCase();
  const year = date.getFullYear();

  return `${day} ${month} ${year}`;
}

loadData();
