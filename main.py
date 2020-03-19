from strategies import SwervePlayer
from strategies import StraightPlayer
from game import game

# Payoff matrix
matrix = {
    True: {
        True: (0, 0),
        False: (-1, 1)
    },
    False: {
        True: (1, -1),
        False: (-1000, -1000)
    }
}

# Total number of players
players = {
    SwervePlayer: 10,
    StraightPlayer: 10
}

# Exclusion and number of rounds
exclusion = 0.2
rounds = 10

# Stopping criterion
def stopping(generation, ps):
    def same_type(arr):
        t = type(arr[0])
        for el in arr:
            if type(el) is not t:
                return False
        return True

    if generation > 10 or same_type(ps):
        return True

    return False

def fitness(ps):
    return sum(p.score for p in ps)

# Start game
game(
    payoffs=matrix,
    players=players,
    exclusion=exclusion,
    rounds=rounds,
    stopping=stopping,
    fitness=fitness
)


