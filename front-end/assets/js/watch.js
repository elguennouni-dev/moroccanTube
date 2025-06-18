document.addEventListener('DOMContentLoaded', () => {
    const videoPlayer = document.getElementById('video-player');
    const videoTitle = document.getElementById('video-title');
    const videoDescription = document.getElementById('video-description');
    const videoUploader = document.getElementById('video-uploader');
    const videoViews = document.getElementById('video-views');
    const likeButton = document.getElementById('like-button');
    const likeIcon = likeButton.querySelector('i');
    const likeCountSpan = document.getElementById('like-count');

    const MOCK_VIDEO_DATA = [
        {
            id: "v1",
            src: "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", // Sample MP4
            title: "Journey Through the Atlas Mountains",
            description: "An immersive journey through the majestic Atlas Mountains, showcasing breathtaking landscapes and vibrant local culture. Experience the serene beauty of Morocco's highlands.",
            uploader: "MoroccoExplorer",
            views: "1,234,567",
            likes: 12500,
            isLiked: false
        },
        {
            id: "v2",
            src: "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
            title: "Fes Medina: A Walk Through History",
            description: "Step back in time as we explore the ancient medina of Fes, a UNESCO World Heritage site. Discover hidden souks, traditional craftsmanship, and the rich history of this vibrant city.",
            uploader: "HeritageTrails",
            views: "870,321",
            likes: 9500,
            isLiked: false
        },
        {
            id: "v3",
            src: "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
            title: "Cooking Tagine Like a Local",
            description: "Join Chef Aisha as she guides you through the secrets of preparing an authentic Moroccan Tagine. Learn about the spices, ingredients, and traditional cooking methods.",
            uploader: "ChefAisha",
            views: "2,500,100",
            likes: 25000,
            isLiked: true
        },
        {
            id: "v4",
            src: "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
            title: "Sahara Desert Camping Experience",
            description: "An unforgettable adventure into the heart of the Sahara Desert. Witness stunning sunsets, ride camels over dunes, and enjoy traditional Berber hospitality under the stars.",
            uploader: "DesertNomads",
            views: "1,550,789",
            likes: 18000,
            isLiked: false
        }
    ];

    const MOCK_LIKE_API_ENDPOINT = 'https://mockapi.example.com/toggle-like';

    const urlParams = new URLSearchParams(window.location.search);
    const videoId = urlParams.get('id');

    let currentVideo = null;

    if (videoId) {
        currentVideo = MOCK_VIDEO_DATA.find(video => video.id === videoId);
        if (currentVideo) {
            videoPlayer.src = currentVideo.src;
            videoTitle.textContent = currentVideo.title;
            videoDescription.textContent = currentVideo.description;
            videoUploader.textContent = currentVideo.uploader;
            videoViews.textContent = `${currentVideo.views} views`;
            likeCountSpan.textContent = currentVideo.likes;
            if (currentVideo.isLiked) {
                likeIcon.classList.remove('far');
                likeIcon.classList.add('fas', 'text-red-500');
            } else {
                likeIcon.classList.remove('fas', 'text-red-500');
                likeIcon.classList.add('far');
            }
        } else {
            videoTitle.textContent = 'Video Not Found';
            videoDescription.textContent = 'The requested video could not be found.';
            videoUploader.textContent = '';
            videoViews.textContent = '';
            likeButton.style.display = 'none';
        }
    } else {
        videoTitle.textContent = 'No Video ID Provided';
        videoDescription.textContent = 'Please provide a video ID in the URL (e.g., watch.html?id=v1).';
        videoUploader.textContent = '';
        videoViews.textContent = '';
        likeButton.style.display = 'none';
    }

    likeButton.addEventListener('click', async () => {
        if (!currentVideo) return;

        const originalLikeState = currentVideo.isLiked;
        const originalLikeCount = currentVideo.likes;

        currentVideo.isLiked = !currentVideo.isLiked;
        currentVideo.likes += currentVideo.isLiked ? 1 : -1;
        likeCountSpan.textContent = currentVideo.likes;
        if (currentVideo.isLiked) {
            likeIcon.classList.remove('far');
            likeIcon.classList.add('fas', 'text-red-500');
        } else {
            likeIcon.classList.remove('fas', 'text-red-500');
            likeIcon.classList.add('far');
        }

        try {
            const response = await new Promise(resolve => setTimeout(() => {
                const success = Math.random() > 0.1;
                resolve({ success: success });
            }, 500));

            if (!response.success) {
                currentVideo.isLiked = originalLikeState;
                currentVideo.likes = originalLikeCount;
                likeCountSpan.textContent = currentVideo.likes;
                if (currentVideo.isLiked) {
                    likeIcon.classList.remove('far');
                    likeIcon.classList.add('fas', 'text-red-500');
                } else {
                    likeIcon.classList.remove('fas', 'text-red-500');
                    likeIcon.classList.add('far');
                }
                console.error('Failed to update like status on server.');
            }
        } catch (error) {
            currentVideo.isLiked = originalLikeState;
            currentVideo.likes = originalLikeCount;
            likeCountSpan.textContent = currentVideo.likes;
            if (currentVideo.isLiked) {
                likeIcon.classList.remove('far');
                likeIcon.classList.add('fas', 'text-red-500');
            } else {
                likeIcon.classList.remove('fas', 'text-red-500');
                likeIcon.classList.add('far');
            }
            console.error('Network error during like toggle:', error);
        }
    });
});
