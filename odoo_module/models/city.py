# -*- coding: utf-8 -*-
from openerp import models, fields, api

class City(models.Model):
    _name = 'batoi_logic.city'

    name = fields.Char(string='City Name', size=60, required=True)
    province = fields.Char(string='Province', size=60, required=True)
    postal_codes = fields.One2many('batoi_logic.postal_code', 'city_id', 'Postal Codes')

    _sql_constraints = [
        ('city_province_unique',
         'UNIQUE (name, province)',
         'City of the province already inserted!')]
