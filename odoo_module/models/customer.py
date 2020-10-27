# -*- coding: utf-8 -*-
from odoo import models, fields, api

class customer(models.Model):
    _name = 'batoi_logic.customer'
    _inherit = 'batoi_logic.person'

    addresses = fields.One2many('batoi_logic.address', 'customer_id', 'Associated Addresses')
    orders = fields.One2many('batoi_logic.order','customer_id','Associated Orders')
