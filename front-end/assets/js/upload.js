document.addEventListener('DOMContentLoaded', () => {
    const uploadForm = document.getElementById('upload-form');
    const videoFile = document.getElementById('video-file');
    const uploadProgressContainer = document.getElementById('upload-progress-container');
    const uploadProgressBar = document.getElementById('upload-progress-bar');
    const uploadStatus = document.getElementById('upload-status');

    const MOCK_UPLOAD_API_ENDPOINT = 'https://mockapi.example.com/upload-video';

    uploadForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        uploadStatus.textContent = '';
        uploadProgressContainer.classList.add('hidden');
        uploadProgressBar.style.width = '0%';

        const title = document.getElementById('title').value.trim();
        const description = document.getElementById('description').value.trim();
        const file = videoFile.files[0];

        if (!file) {
            uploadStatus.textContent = 'Please select a video file.';
            uploadStatus.classList.remove('text-green-600');
            uploadStatus.classList.add('text-red-600');
            return;
        }

        if (file.type !== 'video/mp4') {
            uploadStatus.textContent = 'Only MP4 video files are allowed.';
            uploadStatus.classList.remove('text-green-600');
            uploadStatus.classList.add('text-red-600');
            return;
        }

        const formData = new FormData();
        formData.append('title', title);
        formData.append('description', description);
        formData.append('video', file);

        uploadProgressContainer.classList.remove('hidden');
        uploadStatus.textContent = 'Uploading...';
        uploadStatus.classList.remove('text-red-600');
        uploadStatus.classList.add('text-neutral-700');

        let progress = 0;
        const interval = setInterval(() => {
            progress += 5;
            if (progress <= 100) {
                uploadProgressBar.style.width = `${progress}%`;
            }
            if (progress >= 100) {
                clearInterval(interval);
            }
        }, 100);

        try {
            const mockResponse = await new Promise(resolve => setTimeout(() => {
                resolve({
                    success: true,
                    message: 'Video uploaded successfully!',
                    videoUrl: 'mock-video-url.mp4'
                });
            }, 2000));

            clearInterval(interval);
            uploadProgressBar.style.width = '100%';

            if (mockResponse.success) {
                uploadStatus.textContent = mockResponse.message;
                uploadStatus.classList.remove('text-red-600');
                uploadStatus.classList.add('text-green-600');
                uploadForm.reset();
                setTimeout(() => {
                    uploadStatus.textContent = '';
                    uploadProgressContainer.classList.add('hidden');
                }, 3000);
            } else {
                uploadStatus.textContent = mockResponse.message;
                uploadStatus.classList.remove('text-green-600');
                uploadStatus.classList.add('text-red-600');
            }

        } catch (error) {
            clearInterval(interval);
            console.error('Upload error:', error);
            uploadStatus.textContent = 'An error occurred during upload. Please try again.';
            uploadStatus.classList.remove('text-green-600');
            uploadStatus.classList.add('text-red-600');
        }
    });
});
