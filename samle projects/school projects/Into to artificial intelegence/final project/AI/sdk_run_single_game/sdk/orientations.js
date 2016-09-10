var orientations = {
    North: 1,
    East: 2,
    South: 3,
    West: 4,
    reverse: function (orientation) {
        switch (orientation) {
            case this.North:
                return this.South;
            case this.South:
                return this.North;
            case this.West:
                return this.East;
            case this.East:
                return this.West;
        }
    },
    counterclockwise: function (orientation) {
        switch (orientation) {
            case this.North:
                return this.West;
            case this.South:
                return this.East;
            case this.West:
                return this.South;
            case this.East:
                return this.North;
        }
    },
    clockwise: function (orientation) {
        switch (orientation) {
            case this.North:
                return this.East;
            case this.South:
                return this.West;
            case this.West:
                return this.North;
            case this.East:
                return this.South;
        }
    }
};

module.exports = orientations;