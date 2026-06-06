// Dashboard client JS (externalized from dashboard.html)
// Provides: completeAppointment(btn), cancelAppointment(btn), and chart init

async function completeAppointment(btn) {
    const id = btn.getAttribute('data-id');
    if (!id) return;
    if (!confirm('確定要標記這筆掛號為完成嗎？')) return;
    try {
        const res = await fetch('/api/appointments/' + id + '/status', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: 'COMPLETED' })
        });
        if (res.ok) {
            location.reload();
        } else {
            const text = await res.text();
            alert('更新失敗：' + res.status + ' ' + text);
        }
    } catch (e) {
        alert('網路錯誤：' + e);
    }
}

async function cancelAppointment(btn) {
    const id = btn.getAttribute('data-id');
    if (!id) return;
    if (!confirm('確定要取消這筆掛號嗎？')) return;
    try {
        const res = await fetch('/api/appointments/' + id + '/status', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: 'CANCELLED' })
        });
        if (res.ok) {
            location.reload();
        } else {
            const text = await res.text();
            alert('取消失敗：' + res.status + ' ' + text);
        }
    } catch (e) {
        alert('網路錯誤：' + e);
    }
}

// initialize Chart.js bar chart using DASHBOARD_BY_STATUS global object
function initStatusChart() {
    try {
        const booked = (typeof DASHBOARD_BY_STATUS !== 'undefined' && DASHBOARD_BY_STATUS.BOOKED) ? DASHBOARD_BY_STATUS.BOOKED : 0;
        const completed = (typeof DASHBOARD_BY_STATUS !== 'undefined' && DASHBOARD_BY_STATUS.COMPLETED) ? DASHBOARD_BY_STATUS.COMPLETED : 0;
        const cancelled = (typeof DASHBOARD_BY_STATUS !== 'undefined' && DASHBOARD_BY_STATUS.CANCELLED) ? DASHBOARD_BY_STATUS.CANCELLED : 0;

        const ctx = document.getElementById('statusChart');
        if (ctx && window.Chart) {
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['BOOKED','COMPLETED','CANCELLED'],
                    datasets: [{
                        label: '數量',
                        data: [booked, completed, cancelled],
                        backgroundColor: ['#F59E0B','#10B981','#94A3B8']
                    }]
                },
                options: { responsive: true, maintainAspectRatio: false }
            });
        }
    } catch (e) {
        console.error('initStatusChart error', e);
    }
}

// run chart init on DOMContentLoaded
document.addEventListener('DOMContentLoaded', function() {
    initStatusChart();
});
