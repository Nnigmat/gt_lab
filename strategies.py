class BasePlayer:
    def __init__(self):
        self.score = 0
        self.type = 'Base'

    def play(self, other):
        pass

    def upd_score(self, upd):
        self.score += upd

    def get_type(self):
        return 'Base'


class StraightPlayer(BasePlayer):
    def play(self):
        return True

    def get_type(self):
        return 'Straight'



class SwervePlayer(BasePlayer):
    def play(self):
        return False

    def get_type(self):
        return 'Swerve'



