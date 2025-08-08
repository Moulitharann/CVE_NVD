async function fetchCveDetails(cveId) {
  const detailsDiv = document.getElementById("cveDetails");
  detailsDiv.innerHTML = "<p>Loading...</p>";

  try {
    const res = await fetch(`http://localhost:3000/api/cves/${cveId}`);
    const cve = await res.json();

    // Handle backend error JSON
    if (cve.error) {
      detailsDiv.innerHTML = `<p style="color:red;">Error: ${cve.error}</p>`;
      return;
    }

    // Ensure descriptions is an array before using .find()
    let descriptionsArray = Array.isArray(cve.descriptions)
        ? cve.descriptions
        : [];

    let descriptionObj =
        descriptionsArray.find(d => d.lang === "en") ||
        descriptionsArray[0] ||
        {};
    const description = descriptionObj.value || "No description available.";

    const cvssMetric = cve.raw_data?.metrics?.cvssMetricV2?.[0];
    const cvss = cvssMetric?.cvssData || {};

    let cpeItems = [];
    if (Array.isArray(cve.configurations)) {
      cve.configurations.forEach(config => {
        (config.nodes || []).forEach(node => {
          if (Array.isArray(node.cpeMatch)) {
            cpeItems = cpeItems.concat(node.cpeMatch);
          }
        });
      });
    }

    const html = `
      <h2>${cve.cve_id}</h2>
      <p><strong>Description:</strong> ${description}</p>
      
      <h3>CVSS V2 Metrics:</h3>
      <p><strong>Severity:</strong> ${cvssMetric?.baseSeverity ?? "N/A"} | 
         <strong>Score:</strong> ${cvss.baseScore ?? "N/A"}</p>
      <p><strong>Vector String:</strong> ${cvss.vectorString ?? "N/A"}</p>

      <table border="1" cellpadding="5" cellspacing="0">
        <tr>
          <th>Access Vector</th>
          <th>Access Complexity</th>
          <th>Authentication</th>
          <th>Confidentiality Impact</th>
          <th>Integrity Impact</th>
          <th>Availability Impact</th>
        </tr>
        <tr>
          <td>${cvss.accessVector ?? "N/A"}</td>
          <td>${cvss.accessComplexity ?? "N/A"}</td>
          <td>${cvss.authentication ?? "N/A"}</td>
          <td>${cvss.confidentialityImpact ?? "N/A"}</td>
          <td>${cvss.integrityImpact ?? "N/A"}</td>
          <td>${cvss.availabilityImpact ?? "N/A"}</td>
        </tr>
      </table>

      <h3>Scores:</h3>
      <p><strong>Exploitability Score:</strong> ${cvssMetric?.exploitabilityScore ?? "N/A"}</p>
      <p><strong>Impact Score:</strong> ${cvssMetric?.impactScore ?? "N/A"}</p>

      <h3>CPE:</h3>
      <table border="1" cellpadding="5" cellspacing="0">
        <tr><th>Criteria</th><th>Match Criteria ID</th><th>Vulnerable</th></tr>
        ${
        cpeItems.length > 0
            ? cpeItems
                .map(
                    (item) => `
                  <tr>
                    <td>${item.criteria}</td>
                    <td>${item.matchCriteriaId}</td>
                    <td>${item.vulnerable ? "Yes" : "No"}</td>
                  </tr>`
                )
                .join("")
            : `<tr><td colspan="3">No CPE data available.</td></tr>`
    }
      </table>
    `;

    detailsDiv.innerHTML = html;
  } catch (err) {
    detailsDiv.innerHTML = `<p style="color:red;">Error: ${err.message}</p>`;
  }
}
