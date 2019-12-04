import { Body, Controller, Delete, Get, HttpException, Param, Post, Put } from '@nestjs/common';
import { AppService } from './app.service';

// controller of app.service
// @author von Kirschbaum und Strobel
export interface IDelivery {
  destination: string;
  temp: number;
}

export interface IDeliveryUpdate {
  lat: number;
  lon: number;
}

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {
  }

  @Post(':id')
  async createJob(@Param('id') id: number, @Body()delivery: IDelivery) {
    // Input validation
    if (id === null || id === undefined || id < 0 || id > 2) {
      throw new HttpException('Id is out of range or not defined', 400); // bad request
    }
    if (delivery.destination === null || delivery.destination === undefined) {
      throw new HttpException('Destination is not defined', 400); // Bad request
    }
    await this.appService.createJob(id, delivery.destination, delivery.temp);
  }

  @Delete()
  deleteJob(@Body('id') id: number) {
    // Input validation
    if (id === null || id === undefined || id < 0 || id > 2) {
      throw new HttpException('Id is out of range or not defined', 400); // bad request
    }
    this.appService.deleteJob(id);
  }

  @Put(':id')
  updateJob(@Param('id') id: number, @Body()delivery: IDeliveryUpdate) {
    // Input validation
    if (id === null || id === undefined || id < 0 || id > 2) {
      throw new HttpException('Id is out of range or not defined', 400); // bad request
    }
    if (delivery.lat === null || delivery.lat === undefined || delivery.lat < -90 || delivery.lat > 90) {
      throw new HttpException('Lat is out of range or not defined', 400);
    }
    if (delivery.lon === null || delivery.lon === undefined || delivery.lon < -180 || delivery.lon > 180) {
      throw new HttpException('Lon is out of range or not defined', 400);
    }
    this.appService.updateJob(id, delivery.lat, delivery.lon);
  }
}
