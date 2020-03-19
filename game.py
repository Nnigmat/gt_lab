from collections import Counter

def count_types(players):
    res = Counter()
    for p in players:
        res[p.get_type()] += 1

    return res


def game(payoffs, players, exclusion=0.1, rounds=10, stopping=None):
    # Create players
    ps = []
    for key, value in players.items():
        ps.extend([key() for i in range(value)])

    # Count number of players to exclude after each generation
    to_exclude = int(sum(players.values()) * exclusion)

    generation = 1
    while not stopping(generation, ps):
        print(f'Generation: {generation}. Current players distribution: {count_types(ps)}')
        # Games between all players
        for i, player1 in enumerate(ps[:-1]):
            for player2 in ps[i+1:]:
                for r in range(rounds):
                    upd1, upd2 = payoffs[player1.play()][player2.play()]
                    player1.upd_score(upd1)
                    player2.upd_score(upd2)

        # Remove worst players
        scores = sorted([(p.score, i) for i, p in enumerate(ps)], key=lambda x: x[0])
        tmp = []
        for _, i in scores[to_exclude:]:
            tmp.append(ps[i])

        # Populate new players
        for _, i in scores[-to_exclude:]:
            # Create object of type winners and add to array
            tmp.append(type(ps[i])())

        ps = tmp
        generation += 1
    print(f'Generation: {generation}. Current players distribution: {count_types(ps)}')

    return ps

