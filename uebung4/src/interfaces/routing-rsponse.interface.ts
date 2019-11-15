export interface IRoutingRsponse {
  response: {
    route: Array<{
      summary: {
        trafficTime: number
        travelTime: number,
      },
    }>,
  };
}
