# -*- coding: utf-8 -*-
from openerp import models, fields, api

class location(models.Model):
    _name = 'batoi_logic.location'
    latitude = fields.Float(String='Latitude', required=True)
    longitude = fields.Float(String='Longitude', required=True)
    delivery_man_id = fields.Many2one('batoi_logic.delivery_man', 'Delivery Man', required=True)
