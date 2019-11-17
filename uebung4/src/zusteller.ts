export class Zusteller {
  constructor(public readonly name: string, public readonly lamp: number) {
    this.status = ZustellerState.DELIVERED;
  }

  status: ZustellerState;
  startDelivery: number;
  stopDelivery: number;
  temperature: number;
  lat: number;
  lon: number;
  latDestination: number;
  lonDestination: number;
}

export enum ZustellerState {
  READY,
  DELIVERING,
  DELIVERED,
}
