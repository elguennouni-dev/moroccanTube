const videos = [
  {
    id: "v1",
    thumbnail:
      "https://placehold.co/320x180/EEDDCC/36454F?text=Atlas+Mountains",
    title: "Journey Through the Atlas Mountains",
    views: "1.2M views",
    uploader: "MoroccoExplorer",
  },
  {
    id: "v2",
    thumbnail: "https://placehold.co/320x180/DDAACC/36454F?text=Fes+Medina",
    title: "Fes Medina: A Walk Through History",
    views: "870K views",
    uploader: "HeritageTrails",
  }
];

function loadVideos() {
  const videoGrid = document.getElementById("video-grid");
  videoGrid.innerHTML = "";

  videos.forEach((video) => {
    const videoCard = document.createElement("div");
    videoCard.className = `glass-card rounded-xl overflow-hidden shadow-lg hover:shadow-xl transform hover:-translate-y-1 transition duration-300 cursor-pointer
                                   ${
                                     document.body.classList.contains("dark")
                                       ? "dark:bg-gray-800/30 dark:border-gray-700 dark:shadow-xl"
                                       : ""
                                   }`;
    videoCard.setAttribute("data-video-id", video.id);

    videoCard.innerHTML = `
                <img src="${video.thumbnail}" alt="${video.title}" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-lg font-semibold text-blue-900 mb-2 dark:text-orange-300">${video.title}</h3>
                    <p class="text-sm text-neutral-600 mb-1 dark:text-gray-400">${video.views}</p>
                    <p class="text-sm text-amber-700 font-medium dark:text-amber-500">${video.uploader}</p>
                </div>
            `;

    videoCard.addEventListener("click", () => {
      window.location.href = `watch.html?id=${video.id}`;
    });

    videoGrid.appendChild(videoCard);
  });
}

const themeToggleBtn = document.getElementById("theme-toggle");
const sunIcon = document.getElementById("sun-icon");
const moonIcon = document.getElementById("moon-icon");

const prefersDarkMode = window.matchMedia(
  "(prefers-color-scheme: dark)"
).matches;
const savedTheme = localStorage.getItem("theme");

function applyTheme(theme) {
  if (theme === "dark") {
    document.body.classList.add("dark");
    moonIcon.classList.add("hidden");
    sunIcon.classList.remove("hidden");
  } else {
    document.body.classList.remove("dark");
    moonIcon.classList.remove("hidden");
    sunIcon.classList.add("hidden");
  }
  loadVideos();
}

if (savedTheme) {
  applyTheme(savedTheme);
} else if (prefersDarkMode) {
  applyTheme("dark");
} else {
  applyTheme("light");
}

themeToggleBtn.addEventListener("click", () => {
  if (document.body.classList.contains("dark")) {
    applyTheme("light");
    localStorage.setItem("theme", "light");
  } else {
    applyTheme("dark");
    localStorage.setItem("theme", "dark");
  }
});

document.addEventListener("DOMContentLoaded", () => {
  loadVideos();
});
