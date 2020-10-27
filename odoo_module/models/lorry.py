# -*- coding: utf-8 -*-
from openerp import models, fields, api

class lorry(models.Model):
    _name = 'batoi_logic.lorry'

    name = fields.Char(String='Name', size=20, required=True)
    license_plate = fields.Char(String='License Plate', size=10, required=True)
    capacity = fields.Integer(String='Capacity', required=True)
    current_load = fields.Integer(String='Current Load')

    _sql_constraints = [
        ('batoi_logic_unique_license_plate', 'UNIQUE (license_plate)', 'The license plate must be unique')]
