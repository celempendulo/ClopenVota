// Define base URL variable
let baseURL = "https://vota.today/";

// Function to display a toast notification
function showToast(message, type) {
  // Do nothing for now
  /*
  const toastContainer = document.getElementById('toast-container');
  const toast = document.createElement('div');
  toast.className = `toast align-items-center text-white bg-${type}`;
  toast.setAttribute('role', 'alert');
  toast.setAttribute('aria-live', 'assertive');
  toast.setAttribute('aria-atomic', 'true');
  toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>`;
  toastContainer.appendChild(toast);

  // Show the toast
  new bootstrap.Toast(toast).show();
  */
}


// Function to render contesting parties
function renderParties() {
  // Fetch contesting parties from the server
  fetchContestingParties()
  .then(parties => {
    // Render the parties on the page
    displayParties(parties);
  })
  .catch(error => {
    console.error('Error fetching and rendering parties:', error);
    showToast('Failed to fetch and render contesting parties. Please try again later.', 'error');
  });
}

// Function to fetch contesting parties from the server
function fetchContestingParties() {
  return fetch(`contest/parties`)
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to fetch contesting parties');
    }
    return response.json();
  });
}

// Function to display contesting parties on the page
function displayParties(parties) {
  const partyListDiv = document.getElementById('partyList');
  partyListDiv.innerHTML = ''; // Clear previous party list data

  parties.forEach(party => {
    // Create HTML card for each party and append to partyListDiv
    const partyCard = createPartyCard(party);
    partyListDiv.innerHTML += partyCard;
  });

  // Check voter status after rendering parties
  checkVoterStatus();
}

// Function to create HTML card for a party
function createPartyCard(party) {
  // Create HTML for party card and return it
  return `
        <div class="card mb-3">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="card-title">${party.name}</h5>
                    <img src="${party.logoUrl}" alt="${party.name} Logo" class="party-logo">
                </div>
                <button class="btn btn-primary vote-button" onclick="voteForParty('${party.id}')">Vote</button>
            </div>
        </div>
    `;
}

// Function to handle voting for a party
function voteForParty(partyId) {
  // Send POST request to vote for the party
  fetch(`contest/vote/${partyId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (response.ok) {
      // Vote submitted successfully
      showToast('Vote submitted successfully!', 'success');
      // Update UI
      disableVoteButtons();
      enableUndoVoteButton();
    } else {
      // Failed to submit vote
      throw new Error('Failed to submit vote');
    }
  })
  .catch(error => {
    console.error('Error voting for party:', error);
    showToast('Failed to submit vote. Please try again later.', 'error');
  });
}

// Function to disable vote buttons
function disableVoteButtons() {
  const voteButtons = document.querySelectorAll('.vote-button');
  voteButtons.forEach(button => {
    button.disabled = true;
  });
}

// Function to enable undo vote button
function enableUndoVoteButton() {
  const undoVoteButton = document.getElementById('undoVoteButton');
  if (undoVoteButton) {
    undoVoteButton.disabled = false;
  }
}

// Function to check voter status
function checkVoterStatus() {
  // Fetch voter status from the server
  fetchVoterStatus()
  .then(voterStatus => {
    // Update UI based on voter status
    if (voterStatus.voted) {
      disableVoteButtons();
      enableUndoVoteButton();
    } else {
      enableVoteButtons();
      disableUndoVoteButton();
    }
  })
  .catch(error => {
    console.error('Error fetching and updating voter status:', error);
    showToast('Failed to fetch and update voter status. Please refresh the page.', 'error');
  });
}

// Function to fetch voter status from the server
function fetchVoterStatus() {
  return fetch(`contest/vote/voterStatus`)
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to fetch voter status');
    }
    return response.json();
  });
}

// Function to enable vote buttons
function enableVoteButtons() {
  const voteButtons = document.querySelectorAll('.vote-button');
  voteButtons.forEach(button => {
    button.disabled = false;
  });
}

// Function to disable undo vote button
function disableUndoVoteButton() {
  const undoVoteButton = document.getElementById('undoVoteButton');
  if (undoVoteButton) {
    undoVoteButton.disabled = true;
  }
}

// Function to handle undoing a vote
function undoVote() {
  // Send DELETE request to undo vote
  fetch(`contest/vote/`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.status === 202) {
      // Vote undone successfully
      showToast('Vote undone successfully!', 'success');
      // Update UI
      enableVoteButtons();
      disableUndoVoteButton();
    } else {
      // Failed to undo vote
      throw new Error('Failed to undo vote');
    }
  })
  .catch(error => {
    console.error('Error undoing vote:', error);
    showToast('Failed to undo vote. Please try again later.', 'error');
  });
}

// Function to show scoreboard view
function displayScoreboard() {
  // Show scoreboard view and hide voting view
  document.getElementById('votingView').style.display = 'none';
  document.getElementById('scoreboardView').style.display = 'block';

  // Fetch and render scoreboard data
  fetchAndRenderScoreboard();
}

// Function to show voting view
function showVotingView() {
  // Show voting view and hide scoreboard view
  document.getElementById('scoreboardView').style.display = 'none';
  document.getElementById('votingView').style.display = 'block';

  // Fetch and render contesting parties
  renderParties();
}

// Function to display scoreboard
function fetchAndRenderScoreboard() {
  // Fetch scoreboard data from the server
  fetchScoreboard()
  .then(scoreboardData => {
    // Display scoreboard data on the page
    renderScoreboard(scoreboardData);
  })
  .catch(error => {
    console.error('Error fetching and displaying scoreboard:', error);
    showToast('Failed to fetch and display scoreboard. Please try again later.', 'error');
  });
}

// Function to fetch scoreboard data from the server
function fetchScoreboard() {
  return fetch(`contest/scoreboard`)
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to fetch scoreboard');
    }
    return response.json();
  });
}

// Function to render scoreboard data on the page
function renderScoreboard(scoreboardData) {
  const scoreboardDiv = document.getElementById('scoreboard');
  scoreboardDiv.innerHTML = ''; // Clear previous scoreboard data

  // Render scoreboard
  scoreboardData.partyScores.forEach(partyScore => {
    const partyScoreCard = `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${partyScore.party.name}</h5>
                    <p class="card-text">Votes: ${partyScore.votes}</p>
                    <p class="card-text">Vote Percentage: ${partyScore.votesPercentage}</p>
                </div>
            </div>
        `;
    scoreboardDiv.innerHTML += partyScoreCard;
  });

  // Add total score
  const totalScoreCard = `
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Total Votes</h5>
                <p class="card-text">Total: ${scoreboardData.totalScores}</p>
            </div>
        </div>
    `;
  scoreboardDiv.innerHTML += totalScoreCard;
}

// Event listener for loading the page
window.addEventListener('load', () => {
  // Render contesting parties on page load
  renderParties();
});