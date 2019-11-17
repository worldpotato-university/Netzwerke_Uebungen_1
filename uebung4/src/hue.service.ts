import { HttpService } from '@nestjs/common';

export enum LampColor {
  OFF,
  GREEN,
  ORANGE,
  BLUE_BLINKING,
}

export class HueService {
  constructor(private readonly httpService: HttpService) {
  }

  public async color(lamp: number, color: LampColor) {
    console.log(`Lamp: ${lamp}; Color: ${LampColor[color]}`);
    // TODO
  }
}
