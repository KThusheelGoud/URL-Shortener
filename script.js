function shorten() {
  const url = document.getElementById("url").value;
  const shortcode = document.getElementById("shortcode").value;
  const validity = parseInt(document.getElementById("validity").value);

  const payload = {
    url: url,
    shortcode: shortcode || undefined,
    validity: validity || undefined,
  };

  fetch("http://localhost:8080/shorten/custom", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  })
    .then((res) => res.json())
    .then((data) => {
      document.getElementById(
        "result"
      ).innerText = `Short Link: ${data.shortLink}\nExpiry: ${data.expiry}`;
    })
    .catch((err) => {
      document.getElementById("result").innerText = `Error: ${err}`;
    });
}

function getAnalytics() {
  const code = document.getElementById("analyticsCode").value;

  fetch("http://localhost:8080/shorten/analytics/" + code)
    .then((res) => res.json())
    .then((data) => {
      if (data.error) {
        document.getElementById("analyticsResult").innerText = data.error;
      } else {
        document.getElementById(
          "analyticsResult"
        ).innerText = `Original URL: ${data.originalUrl}\nClicks: ${data.clickCount}\nExpiry: ${data.expiry}`;
      }
    })
    .catch((err) => {
      document.getElementById("analyticsResult").innerText = `Error: ${err}`;
    });
}

function shortenRandom() {
  const url = document.getElementById("randomUrl").value;
  const payload = { originalUrl: url };

  fetch("http://localhost:8080/shorten", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  })
    .then((res) => res.json())
    .then((data) => {
      document.getElementById(
        "randomResult"
      ).innerText = `Short Link: ${data.shortLink}`;
    })
    .catch((err) => {
      document.getElementById("randomResult").innerText = `Error: ${err}`;
    });
}
