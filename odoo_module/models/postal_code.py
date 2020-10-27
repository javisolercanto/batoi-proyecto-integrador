# -*- coding: utf-8 -*-
from openerp import models, fields, api

class Postal_code(models.Model):
    _name = 'batoi_logic.postal_code'

    name = fields.Integer(String='Postal code', size=5, required=True)
    address_id = fields.One2many('batoi_logic.address', 'postal_code_id', 'Addresses with this Zip Code')
    city_id = fields.Many2one('batoi_logic.city', 'City', required=True)

    _sql_constraints = [
        ('batoi_logic_name', 'UNIQUE (name)', 'The postal code must be unique')]
