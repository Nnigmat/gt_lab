class BasePlayer:
    def __init__(self):
        self.score = 0
        self.type = 'Base'

    def play(self):
        pass

    def upd_score(self, upd):
        self.score += upd

    def clean_score(self):
        self.score = 0


class StraightPlayer(BasePlayer):
    def __init__(self):
        super().__init__()
        self.type = 'Straight'

    def play(self):
        return True


class SwervePlayer(BasePlayer):
    def __init__(self):
        super().__init__()
        self.type = 'Swerve'

    def play(self):
        return False
